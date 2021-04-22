/**
 * 
 */
package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * @author Admin
 *
 */

public class FileCSVOperation {
	String filePath = "src/data";
	String callerId = "";
	String row = "";
	BufferedReader csvReader;

	public FileCSVOperation(String callerId) {
		this.callerId = callerId;
	}

	public FileCSVOperation() {
		//this.callerId = callerId;
	}

	public List<String> readCSV() {
		List<String> data = new ArrayList<String>();
		try {
			if(callerId != "") {
				//File file = new File(filePath + "/" + callerId + ".csv");
				FileReader fileReader = new FileReader(filePath + "/" + callerId + ".csv");
				csvReader = new BufferedReader(fileReader);
				while ((row = csvReader.readLine()) != null) {
					data.add(row);
				}
				csvReader.close();
			}else{
				System.out.println("Caller id is not setup");
			}
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			return data;
		}
	}

	public List<String> readCSV(File file) {
		List<String> data = new ArrayList<String>();
		try {
			if(file != null) {
				FileReader fileReader = new FileReader(file);
				csvReader = new BufferedReader(fileReader);
				while ((row = csvReader.readLine()) != null) {
					data.add(row);
				}
				csvReader.close();
			}else{
				System.out.println("File is not available");
			}
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			return data;
		}
	}

	public <T> boolean writeCSV(Map<String, T> arg) {
		try {
			FileWriter fileWriter = new FileWriter(filePath + "/" + callerId + ".csv");
			for (Map.Entry<String, T> objSet : arg.entrySet()) {
				T obj = objSet.getValue();
				fileWriter.append(obj.toString());
				fileWriter.append("\n");
			}
			fileWriter.flush();
			fileWriter.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
