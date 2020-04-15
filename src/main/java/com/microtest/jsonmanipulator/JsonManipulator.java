package com.microtest.jsonmanipulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.JsonPrimitive;
//import com.google.gson.reflect.TypeToken;
import com.microtest.utils.SwaggerManipulator;

import java.lang.reflect.Type;

public class JsonManipulator {

	// property to hold list of json from excel
	public static ArrayList<JSONObject> jsonList = new ArrayList<>();
	public static ArrayList<JSONObject> newjsonList;

	public JsonManipulator() throws IOException {

		jsonList = generateJsonTemplate();
		writeToJSONFile(jsonList);
	}

	public static ArrayList<JSONObject> generateJsonTemplate() throws IOException {
		// call SwagggerManupulator to get hashmap
		try {
			SwaggerManipulator objSwaggerManipulator = new SwaggerManipulator("./Resource/Test.yml");
			Gson gson = new Gson();
			Type gsonType = new TypeToken<HashMap>() {}.getType();
			String gsonString = gson.toJson(objSwaggerManipulator.yamlMap, gsonType);
			JSONParser parser = new JSONParser();
			JSONObject jsonobject = (JSONObject) parser.parse(gsonString);
			newjsonList = readExcelData(jsonobject);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newjsonList;
	}

	public static JSONObject setNewValuesIntoJsonObject(JSONObject jsonobject, Map<String, Integer> nameIndexMap,
			Row currentRow) throws Exception {
		for (Object keyStr : jsonobject.keySet()) {
			Object keyvalue = jsonobject.get((String) keyStr);

			if (keyvalue instanceof String) {
				if (nameIndexMap.get(keyStr) != null)
					jsonobject.put((String) keyStr, currentRow.getCell(nameIndexMap.get(keyStr)).toString());
			} else if (keyvalue instanceof Integer) {
				if (nameIndexMap.get(keyStr) != null)
					jsonobject.put((String) keyStr, currentRow.getCell(nameIndexMap.get(keyStr)));
			} else if (keyvalue instanceof Map) {
				JSONObject newJson = new JSONObject((Map) keyvalue);
				JSONObject newValue = setNewValuesIntoJsonObject(newJson, nameIndexMap, currentRow);
				jsonobject.put((String) keyStr, newValue);
			} else if (keyvalue instanceof ArrayList) {
				if (nameIndexMap.get(keyStr) != null) {
					Object cellValue = currentRow.getCell(nameIndexMap.get(keyStr.toString()));
					List<String> jsonArrayValue = new ArrayList<String>(Arrays.asList(cellValue.toString().split(",")));
					jsonobject.put((String) keyStr, jsonArrayValue);
				} else {
					List<Object> list = new ArrayList<>();
					if (keyvalue.getClass().isArray()) {
						list = Arrays.asList((Object[]) keyvalue);
						int size = list.size(); //
					} else if (keyvalue instanceof Collection) {
						int i;
						JSONArray jsonArrayValue = new JSONArray();
						JSONObject newValue = null;
						list = new ArrayList<>((Collection<?>) keyvalue);
						for (i = 0; i < list.size(); i++) {
							String arrayValue = list.get(i).toString();
							JSONParser parser = new JSONParser();
							JSONObject parsedJsonObject = (JSONObject) parser.parse(arrayValue);
							newValue = setNewValuesIntoJsonObject(parsedJsonObject, nameIndexMap,currentRow);
							jsonArrayValue.add(newValue);
						}
						jsonobject.put((String) keyStr, jsonArrayValue);
						System.out.println(jsonobject);
					}
				}
			}
		}
		return jsonobject;
	}

	private static void writeToJSONFile(ArrayList<JSONObject> writeJsonList) {

		try (FileWriter file = new FileWriter("./Resource/User.json")) {
			file.write(newjsonList.toString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(writeJsonList);
	}

	public static ArrayList<JSONObject> readExcelData(JSONObject jsonobject) {
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
			while (rowit.hasNext()) {
				Row currentRow = rowit.next();
				int lastCellNumber = currentRow.getLastCellNum();
				JSONObject defaultJson = new JSONObject(jsonobject);
				// defaultJson = To read original yaml file for comparing keys with excel
				// nameIndexMap = will retrieve the columns index and name from excel
				// currentRow = it is holding current row data from excel
				JSONObject newJson = setNewValuesIntoJsonObject(defaultJson, nameIndexMap, currentRow);
				System.out.println(newJson);
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
				map.put(cell.getStringCellValue(), cell.getColumnIndex()); // add the cell contents (name of column) and
																			// cell index to the map
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/*
	 * public static void main(String[] args) throws IOException { JsonManipulator
	 * objJsonManipulator = new JsonManipulator(); }
	 */
}
