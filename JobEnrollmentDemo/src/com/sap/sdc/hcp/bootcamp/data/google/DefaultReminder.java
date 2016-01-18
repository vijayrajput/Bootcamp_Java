package com.sap.sdc.hcp.bootcamp.data.google;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultReminder {
	@SerializedName("method")
	@Expose
	private String method;
	@SerializedName("minutes")
	@Expose
	private Long minutes;

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

	/**
	* 
	* @return
	* The minutes
	*/
	public Long getMinutes() {
	return minutes;
	}

	/**
	* 
	* @param minutes
	* The minutes
	*/
	public void setMinutes(Long minutes) {
	this.minutes = minutes;
	}
}