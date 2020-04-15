package com.microtest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class SwaggerManipulator {
	String filePath;
	FileInputStream sourceFile;
	InputStreamReader sourceFileReader;
	public Map<String, Object> yamlMap = new HashMap<String, Object>();

	public SwaggerManipulator(String newFile) {
		this.filePath = newFile;
		readSwagger();
		parseSwagger();
	}

	public InputStreamReader readSwagger() {
		try {
			sourceFile = new FileInputStream(new File(filePath));
			sourceFileReader = new InputStreamReader(sourceFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourceFileReader;
	}

	public Map<String, Object> parseSwagger() {
		try {
			Yaml yaml = new Yaml();
			for (Object yamlObject : new Yaml().loadAll(sourceFileReader)) {
				yamlMap = (Map) yamlObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return yamlMap;
	}

	/*
	 * public static void main(String[] args) { SwaggerManipulator
	 * objSwaggerManipulator = new SwaggerManipulator("./Resource/Test.yml"); }
	 */
}
