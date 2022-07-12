package Java_assignment.Java_assignment.csvReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReader {
	public static void main(String[] args) throws Exception {
		String raw = "";
		String splitStr = ",";

		List<List<String>> csvList = new ArrayList<>();
		List<List<String>> columnTitleLst = new ArrayList<>();

		String csvDoc = "C:\\Users\\abate\\Desktop\\new 3.csv";
		if (checkFileExt(new File(csvDoc))) {
			// 1. Read CSV file
			BufferedReader br = new BufferedReader(new FileReader(csvDoc));
			int count = 0;
			while ((raw = br.readLine()) != null) {
				if (count == 0) {
					columnTitleLst.add(Arrays.asList(raw.split(splitStr)));
				} else { // Exclude column title
					csvList.add(Arrays.asList(raw.split(splitStr)));
				}
				count++;
			}
			System.out.println(csvList);
		}
	}

	private static boolean checkFileExt(File file) {
		String fName = file.getName();
		String fExt = "";
		String csv = "csv";
		Boolean isCsv = false;
		if (fName.lastIndexOf(".") != 0 && fName.lastIndexOf(".") != -1) {
			fExt = fName.substring(fName.lastIndexOf(".") + 1);
			if (fExt.equals(csv)) {
				isCsv = true;
			}
		}
		return isCsv;
	}
}
