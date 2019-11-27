package com.yodlee.ycc.dapi.utils;

import java.util.List;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class JsonDataHandler {

	private Configuration config;

	private ReadContext context;

	private String json;

	private boolean isNullHandler = false;

	private void init(String jsonDoc) {
		config = Configuration.defaultConfiguration();
		this.json = jsonDoc;
	}

	/**
	 * Parses the json data and allows reading json data
	 * 
	 * @param jsonDoc
	 */
	public void parseJson(String jsonDoc) {
		if (jsonDoc != null && jsonDoc.length() > 0) {
			init(jsonDoc);
			context = JsonPath.using(config).parse(json);

		} else
			isNullHandler = true;
	}

	/**
	 * Fetches list of values from the json data based in json path expression
	 * 
	 * @param jsonPath
	 * @return List of values
	 */
	public List getValues(String jsonPath) {
		if (jsonPath.length() == 0)
			return null;
		List list = context.read(jsonPath, List.class);
		return list;
	}

	public Object getJsonObject(String jsonPath) {
		if (jsonPath.length() == 0)
			return null;
		Object obj = context.read(jsonPath);
		return obj;
	}

	public String getValue(String jsonPath) {
		if (jsonPath.length() == 0)
			return null;
		String str = context.read(jsonPath, String.class);
		return str;
	}

	public boolean isNullHandler() {
		return isNullHandler;
	}
}
