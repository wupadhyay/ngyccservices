package com.yodlee.ycc.dapi.splunkresponseformat;

public class AttributeProperty {
	
	private String attribute;
	private String JsonPath;
	private String type;
	private String description;
	
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getJsonPath() {
		return JsonPath;
	}
	public void setJsonPath(String jsonPath) {
		JsonPath = jsonPath;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isEntity(){
		//type.contains("<ENTITY>");
		return type.contains("<ENTITY>");
	}
	
	public boolean isComponent(){
		//type.contains("<ENTITY>");
		return type.contains("<COMPONENT>");
	}
	public boolean isEnum(){
		//type.contains("<ENTITY>");
		return type.contains("<ENUM>");
	}
	
	
	protected AttributeProperty copy() {
		AttributeProperty prop = new AttributeProperty();
		prop.setAttribute(this.attribute);
		prop.setDescription(this.description);
		prop.setJsonPath(this.JsonPath);
		prop.setType(this.type);
		return prop;
	}

}
