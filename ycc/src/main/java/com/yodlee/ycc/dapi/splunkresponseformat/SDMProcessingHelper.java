package com.yodlee.ycc.dapi.splunkresponseformat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SDMProcessingHelper {
	
	private static String line;
	private final char CSV_COMMA_ESCAPE = '"';
	private  final char COMMA = ',';
	private Map<String, List<AttributeProperty>> entityMap = new LinkedHashMap<String, List<AttributeProperty>>();
	private Map<String, List<AttributeProperty>> componentsMap = new LinkedHashMap<String, List<AttributeProperty>>();
	private Map<String, List<String>> enumMap = new LinkedHashMap<String, List<String>>();

	public  void processFile(File file ) {
			 BufferedReader in;
			 try{
				 in = new BufferedReader(new FileReader(file));
		        } catch (IOException e){
		    	throw new RuntimeException(e.getMessage());
		      }
				boolean isHeader = true;
				String type="";
				List attrList=new ArrayList<AttributeProperty>();
				 List enumList=new ArrayList<String>();
				 String objectName="";
			try{
				while((line = in.readLine()) != null) {			
						String[] columns = getColumns(line);
						if(isHeader){ isHeader=false;
					      type=columns[0];
						   
						}else{
							if(columns[0].length()>0){
							     if(attrList.size()>0||enumList.size()>0){ 
				                        if(type.equalsIgnoreCase("Entity")&&attrList.size()>0){
				                			 entityMap.put(objectName, new ArrayList<AttributeProperty>(attrList));
				                		}
				                        if(type.equalsIgnoreCase("Component")&&attrList.size()>0){
				                        	componentsMap.put(objectName, new ArrayList<AttributeProperty>(attrList));
				                        }
				                        if(type.equalsIgnoreCase("Enumeration")&&enumList.size()>0){
				                        	enumMap.put(objectName, new ArrayList(enumList));
				                        }
				                   }
	                        attrList.clear();
	                        enumList.clear();
	                        objectName=columns[0];
							}
	   					AttributeProperty ap=new AttributeProperty();
						if(columns[0].length()==0&&columns[1].length()!=0&&!type.equalsIgnoreCase("Enumeration")){
								ap.setAttribute(columns[1]);
								ap.setJsonPath(columns[2]);
								ap.setType(columns[3]);
								ap.setDescription(columns[4]);
								attrList.add(ap);
							}if(type.equalsIgnoreCase("Enumeration")&&columns[1].length()!=0&&columns[0].length()==0){
							  enumList.add(columns[1]);
							}
							
						}
				}
				if(type.equalsIgnoreCase("Entity")&&attrList.size()>0){
				 entityMap.put(objectName, new ArrayList<AttributeProperty>(attrList));
			    }
				if(type.equalsIgnoreCase("Component")&&attrList.size()>0){
					componentsMap.put(objectName, new ArrayList<AttributeProperty>(attrList));
				}
				if(type.equalsIgnoreCase("Enumeration")&&enumList.size()>0){
					enumMap.put(objectName, new ArrayList<String>(enumList));
				}
				attrList.clear();
				enumList.clear();
			} catch (IOException e){
		    	throw new RuntimeException(e.getMessage());
		    }
	}

		 
		 public  String[] getColumns(String line) { 
				List<String> columns = new ArrayList<String>();
				char[] chars = line.toCharArray();
				StringBuilder currentColumn = new StringBuilder();
				char currentChar; 
				boolean columnContainsComma = false; 
				for (int i = 0; i < chars.length; i++) {
					currentChar = chars[i];
					if (currentChar == COMMA && !columnContainsComma){
						columns.add(currentColumn.toString().trim());
						currentColumn = new StringBuilder();
						continue;
					}
					if (currentChar == CSV_COMMA_ESCAPE) {
						if(currentColumn.length() == 0 && !columnContainsComma) {  // start of column with comma in it
							columnContainsComma = true;
							continue; 
						}
						if(columnContainsComma && ((i+1 < chars.length && chars[i+1] == COMMA) || i+1 == chars.length)) { // end of column with comma in it
							columnContainsComma = false; 
							continue;
						}
					}
					
					currentColumn.append(currentChar);
				}
				columns.add(currentColumn.toString().trim());
				return columns.toArray(new String[0]);
			}
		 
		 public Map<String, List<AttributeProperty>> getComponentsMap(){
			 return componentsMap;			 
		 }
		 public Map<String, List<AttributeProperty>> getEntityMap(){
			 return entityMap;			 
		 }
		 public Map<String, List<String>> getEnumMap(){
			 return enumMap;			 
		 }
}
