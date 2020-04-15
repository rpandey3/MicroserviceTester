package com.microtest.jsonmanipulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.microtest.utils.SwaggerManupulator;
 

public class JsonManipulator {

	// property to hold list of json from excel
	public  ArrayList<JSONObject> jsonList = new ArrayList<>();
	public ArrayList<JSONObject> newJson;

	public JsonManipulator() throws IOException {

		jsonList = generateJsonTemplate();
		writeToJSONFile(jsonList);
	}

	public static ArrayList<JSONObject> generateJsonTemplate() throws IOException {
		// call SwagggerManupulator to get hashmap
		SwaggerManupulator manupulator = new SwaggerManupulator("./Resource/Test.yml");
		// converte hashmap to jsonobject
		JSONObject json = new JSONObject(manupulator.yamlMap);
		ArrayList<JSONObject> newjsonList = readExcelData(json);
		//newjsonList.get(1);
		return newjsonList;
	}

	public static JSONObject setNewValuesIntoJsonObject(JSONObject json, Map<String, Integer> nameIndexMap,
			Row currentRow) {
		for (Object keyStr : json.keySet()) {
			Object keyvalue = json.get(keyStr);
			// for nested objects iteration if required

			if (keyvalue instanceof String || keyvalue instanceof Integer) {
				if (nameIndexMap.get(keyStr) != null)
					json.put(keyStr, currentRow.getCell(nameIndexMap.get(keyStr)));

			} else if (keyvalue instanceof Map) {
				JSONObject newJson = new JSONObject((Map) keyvalue);
				JSONObject newValue = setNewValuesIntoJsonObject(newJson, nameIndexMap, currentRow);

				json.put(keyStr, newValue);
			} else if (keyvalue instanceof ArrayList) {
//				if (nameIndexMap.get(keyStr) != null)
				json.put(keyStr, currentRow.getCell(nameIndexMap.get(keyStr.toString())));

			}
		}
		return json;

	}

	private static ArrayList<JSONObject> writeToJSONFile(ArrayList<JSONObject> jsonBody) {
		// Write JSON file
		// Create separate json file or new row in single file for each record from
		// excel
		try (FileWriter file = new FileWriter("./Resource/User.json")) {
			file.write(jsonBody.toString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(jsonBody);
		
		return jsonBody;
	}

	public static ArrayList<JSONObject> readExcelData(JSONObject json)  {
		try {
			// call generate JSOMTemplet
			File file = new File("./Resource/TestExcel.xls");
			FileInputStream stream = new FileInputStream(file);
			HSSFWorkbook book = new HSSFWorkbook(stream);
			HSSFSheet sheet = book.getSheet("Sheet1");
			Map<String, Integer> nameIndexMap = new HashMap<String, Integer>();
			Iterator<Row> rowit = sheet.iterator();

			nameIndexMap = getColumnIndexByColumnName(sheet);

			rowit.next();
			ArrayList<JSONObject> jsonListOne = new ArrayList<>();
			;
			// ArrayList<String> excelRows = new ArrayList<String>();
			while (rowit.hasNext()) {
				Row currentRow = rowit.next();
				int lastCellNumber = currentRow.getLastCellNum();
				JSONObject defaultJson = new JSONObject(json);
				// defaultJson = To read original yaml file for comparing keys with excel
				// nameIndexMap = will retrieve the columns index and name from excel
				// currentRow = it is holding current row data from excel
				JSONObject newJson = setNewValuesIntoJsonObject(defaultJson, nameIndexMap, currentRow);
				
				jsonListOne.add(newJson);
			}
			return jsonListOne;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, Integer> getColumnIndexByColumnName(HSSFSheet sheet) {
		// Change the method name
		// get the header name and index of header name
		// Exception handling pending
		Map<String, Integer> map = new HashMap<String, Integer>(); // Create map

		try {
			HSSFCell cell;

			HSSFRow row = sheet.getRow(0); // Get first row
			// following is boilerplate from the java doc
			short firstColIndex = row.getFirstCellNum(); // get the first column index for a row
			short lastColIndex = row.getLastCellNum(); // get the last column index for a row

			for (short colIndex = firstColIndex; colIndex < lastColIndex; colIndex++) { // loop from first to last index
				cell = row.getCell(colIndex); // get the cell
				map.put(cell.getStringCellValue(), cell.getColumnIndex()); // add the cell contents (name of column) and cell index to the map
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) throws IOException {
		JsonManipulator manupulator = new JsonManipulator();
		System.out.println("hi");
	}
}
