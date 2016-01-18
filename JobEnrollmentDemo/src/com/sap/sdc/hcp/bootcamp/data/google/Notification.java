package com.sap.sdc.hcp.bootcamp.data.google;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {
	@SerializedName("type")
	@Expose
	private String type;
	@SerializedName("method")
	@Expose
	private String method;

	/**
	* 
	* @return
	* The type
	*/
	public String getType() {
	return type;
	}

	/**
	* 
	* @param type
	* The type
	*/
	public void setType(String type) {
	this.type = type;
	}

	/**
	* 
	* @return
	* The method
	*/
	public String getMethod() {
	return method;
	}

	/**
	* 
	* @param method
	* The method
	*/
	public void setMethod(String method) {
	this.method = method;
	}
}