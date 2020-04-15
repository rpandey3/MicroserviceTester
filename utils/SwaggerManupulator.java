package com.microtest.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class SwaggerManupulator {
	String filePath;
	File yamlFile;
	public Map<String, Object> yamlMap = new HashMap<String, Object>();

	public SwaggerManupulator(String newFile) {
		 this.filePath = newFile;
		 readSwagger();
		 parseSwagger();
	}

	public File readSwagger() {
		try {
			yamlFile = new File(filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yamlFile;
	}

	public void parseSwagger() {
		try {
			
			ObjectMapper mapper1 = new ObjectMapper(new YAMLFactory());
			ObjectMapper mapper = new ObjectMapper();
			YamlObject ymlobj = mapper1.readValue(yamlFile, YamlObject.class);
			yamlMap = mapper.convertValue(ymlobj, Map.class);
			/*
			 * System.out.println(yamlMap);
			 * 
			 * for (Entry<String, Object> entry : yamlMap.entrySet()) {
			 * System.out.println(entry.getKey()); System.out.println(entry.getValue());
			 * if(entry.getKey().equals("address")) { Map<String, String> addrs =
			 * mapper.convertValue(entry, Map.class); System.out.println(addrs); // for
			 * (Entry<String, Object> entry1 : entry.) {} } }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	  public static void main(String[] args) {
	  
	  SwaggerManupulator manupulator = new
	  SwaggerManupulator("./Resource/Test.yml"); }
	 
}
