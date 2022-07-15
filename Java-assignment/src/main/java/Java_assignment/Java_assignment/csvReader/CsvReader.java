package Java_assignment.Java_assignment.csvReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CsvReader {

	public static void main(String[] args) throws IOException {
		/*
		 ****** fetch from any source: 
		 *List<String> listOfCsvFiles = ServiceLayer.getAllCsvFiles();
		 * if(listOfCsvFiles.size() > 0) { 
		 * 	  for(String csvDoc: listOfCsvFiles){
		 */		
		/* For test purpose, you need to create a csv file, based on the provided data points,
		 * and put the path as follows
		*/
		// Assume this file is fetched from a source:.....
		String csvFile = "C:\\Users\\abate\\Desktop\\new 3.csv";
		if (checkFileExt(new File(csvFile))) {
			List<List<String>> csvList = new ArrayList<>();
			List<List<String>> headerLst = new ArrayList<>();
			String raw = "";
			String splitStr = ",";
			// 1. Read CSV file
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			int count = 0;
			while ((raw = br.readLine()) != null) {
				if (count == 0) {
					headerLst.add(Arrays.asList(raw.split(splitStr)));
				} else { // Exclude column title
					csvList.add(Arrays.asList(raw.split(splitStr)));
				}
				count++;
			}

			// 2. separate by insurance name and then sort by First name and Last name
			if (csvList.size() > 0 && headerLst.size() > 0) {
				List<String> header = headerLst.get(0);
				int uidIndx = getIndex(Enums.USER_ID, header);
				int versIndx = getIndex(Enums.VERSION, header);
				int insIndx = getIndex(Enums.INSURANCE_COMPANY, header);
				List<String> insName = new ArrayList<>();
				for (List<String> lst : csvList) {
					insName.add(lst.get(insIndx));
				}

				insName = new ArrayList<>(new HashSet<>(insName));
				if (insName.size() > 0) {
					computeFinalFilterAndSort(insName, csvList, insIndx, uidIndx, versIndx, header);
				}
			}
		}

		// } // end ...for(String csvDoc: listOfCsvFiles)
		// } // end ...if(listOfCsvFiles.size() > 0)
	}

	private static List<List<String>> computeFinalFilterAndSort(List<String> insName, List<List<String>> csvList,
			int insIndx, int uidIndx, int versIndx, List<String> header) {
		List<List<String>> finalSorted = new ArrayList<>();
		for (String name : insName) {
			List<List<String>> listByInsur = new ArrayList<>();
			for (List<String> obj : csvList) {
				if (name.equals(obj.get(insIndx))) {
					listByInsur.add(obj);
				}
			}
			// if duplicate id for same insurance, take only highest version
			List<List<String>> sortedByDuplId = checkDuplicateId(listByInsur, uidIndx, versIndx);

			// sort by First name and Last name:
			int fNameIndex = getIndex(Enums.FIRST_NAME, header);
			int lNameIndex = getIndex(Enums.LAST_NAME, header);
			finalSorted = sortedByDuplId.stream()
					.sorted((o1, o2) -> o1.get(lNameIndex).compareTo(o2.get(lNameIndex)))
					.sorted((o1, o2) -> o1.get(fNameIndex).compareTo(o2.get(fNameIndex))).collect(Collectors.toList());

			System.out.println("---- Enrolees by insurance company: " + name);
			System.out.println(finalSorted);
		}
		return finalSorted;
	}

	private static boolean checkFileExt(File file) {
		String fileName = file.getName();
		String fileExt = "";
		String csv = "csv";
		boolean isCsvFile = false;
		if (fileName.lastIndexOf(".") != 0 && fileName.lastIndexOf(".") != -1) {
			fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (fileExt.equals(csv)) {
				isCsvFile = true;
			}
		}
		return isCsvFile;
	}

	private static int getIndex(String name, List<String> header) {
		String firstStr = name.split("\\s+")[0];
		int columnTitleIndex = 0;
		//capture the column title index based on first string of the title
		for (int i = 0; i < header.size(); i++) {
			if (header.get(i).toLowerCase().contains(firstStr.toLowerCase())) {
				columnTitleIndex = i;
				break;
			}
		}
		return columnTitleIndex;
	}

	private static List<List<String>> checkDuplicateId(List<List<String>> listByInsur, int uId, int versIndx) {
		List<List<String>> sortedByDuplUId = new ArrayList<>();
		if (listByInsur.size() > 0) {
			// sort by version number to take values of highest Version
			Set<String> depulSet = new HashSet<>();
			List<List<String>> sortedByVersionId = listByInsur.stream()
					.sorted((o2, o1) -> o1.get(versIndx).compareTo(o2.get(versIndx))).collect(Collectors.toList());

			for (List<String> list : sortedByVersionId) {
				boolean notDepul = depulSet.add(list.get(uId));
				if (notDepul) {
					sortedByDuplUId.add(list);
				}
			}
		}
		return sortedByDuplUId;
	}
}
