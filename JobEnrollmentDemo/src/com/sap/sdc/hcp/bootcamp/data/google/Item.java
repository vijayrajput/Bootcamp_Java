package com.sap.sdc.hcp.bootcamp.data.google;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
	@SerializedName("kind")
	@Expose
	private String kind;
	@SerializedName("etag")
	@Expose
	private String etag;
	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("summary")
	@Expose
	private String summary;
	@SerializedName("timeZone")
	@Expose
	private String timeZone;
	@SerializedName("colorId")
	@Expose
	private String colorId;
	@SerializedName("backgroundColor")
	@Expose
	private String backgroundColor;
	@SerializedName("foregroundColor")
	@Expose
	private String foregroundColor;
	@SerializedName("selected")
	@Expose
	private Boolean selected;
	@SerializedName("accessRole")
	@Expose
	private String accessRole;
	@SerializedName("defaultReminders")
	@Expose
	private List<DefaultReminder> defaultReminders = new ArrayList<DefaultReminder>();
	@SerializedName("notificationSettings")
	@Expose
	private NotificationSettings notificationSettings;
	@SerializedName("primary")
	@Expose
	private Boolean primary;

	/**
	* 
	* @return
	* The kind
	*/
	public String getKind() {
	return kind;
	}

	/**
	* 
	* @param kind
	* The kind
	*/
	public void setKind(String kind) {
	this.kind = kind;
	}

	/**
	* 
	* @return
	* The etag
	*/
	public String getEtag() {
	return etag;
	}

	/**
	* 
	* @param etag
	* The etag
	*/
	public void setEtag(String etag) {
	this.etag = etag;
	}

	/**
	* 
	* @return
	* The id
	*/
	public String getId() {
	return id;
	}

	/**
	* 
	* @param id
	* The id
	*/
	public void setId(String id) {
	this.id = id;
	}

	/**
	* 
	* @return
	* The summary
	*/
	public String getSummary() {
	return summary;
	}

	/**
	* 
	* @param summary
	* The summary
	*/
	public void setSummary(String summary) {
	this.summary = summary;
	}

	/**
	* 
	* @return
	* The timeZone
	*/
	public String getTimeZone() {
	return timeZone;
	}

	/**
	* 
	* @param timeZone
	* The timeZone
	*/
	public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
	}

	/**
	* 
	* @return
	* The colorId
	*/
	public String getColorId() {
	return colorId;
	}

	/**
	* 
	* @param colorId
	* The colorId
	*/
	public void setColorId(String colorId) {
	this.colorId = colorId;
	}

	/**
	* 
	* @return
	* The backgroundColor
	*/
	public String getBackgroundColor() {
	return backgroundColor;
	}

	/**
	* 
	* @param backgroundColor
	* The backgroundColor
	*/
	public void setBackgroundColor(String backgroundColor) {
	this.backgroundColor = backgroundColor;
	}

	/**
	* 
	* @return
	* The foregroundColor
	*/
	public String getForegroundColor() {
	return foregroundColor;
	}

	/**
	* 
	* @param foregroundColor
	* The foregroundColor
	*/
	public void setForegroundColor(String foregroundColor) {
	this.foregroundColor = foregroundColor;
	}

	/**
	* 
	* @return
	* The selected
	*/
	public Boolean getSelected() {
	return selected;
	}

	/**
	* 
	* @param selected
	* The selected
	*/
	public void setSelected(Boolean selected) {
	this.selected = selected;
	}

	/**
	* 
	* @return
	* The accessRole
	*/
	public String getAccessRole() {
	return accessRole;
	}

	/**
	* 
	* @param accessRole
	* The accessRole
	*/
	public void setAccessRole(String accessRole) {
	this.accessRole = accessRole;
	}

	/**
	* 
	* @return
	* The defaultReminders
	*/
	public List<DefaultReminder> getDefaultReminders() {
	return defaultReminders;
	}

	/**
	* 
	* @param defaultReminders
	* The defaultReminders
	*/
	public void setDefaultReminders(List<DefaultReminder> defaultReminders) {
	this.defaultReminders = defaultReminders;
	}

	/**
	* 
	* @return
	* The notificationSettings
	*/
	public NotificationSettings getNotificationSettings() {
	return notificationSettings;
	}

	/**
	* 
	* @param notificationSettings
	* The notificationSettings
	*/
	public void setNotificationSettings(NotificationSettings notificationSettings) {
	this.notificationSettings = notificationSettings;
	}

	/**
	* 
	* @return
	* The primary
	*/
	public Boolean getPrimary() {
	return primary;
	}

	/**
	* 
	* @param primary
	* The primary
	*/
	public void setPrimary(Boolean primary) {
	this.primary = primary;
	}
}