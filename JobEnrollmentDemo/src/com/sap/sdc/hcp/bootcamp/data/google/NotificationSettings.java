package com.sap.sdc.hcp.bootcamp.data.google;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationSettings {
	@SerializedName("notifications")
	@Expose
	private List<Notification> notifications = new ArrayList<Notification>();

	/**
	* 
	* @return
	* The notifications
	*/
	public List<Notification> getNotifications() {
	return notifications;
	}

	/**
	* 
	* @param notifications
	* The notifications
	*/
	public void setNotifications(List<Notification> notifications) {
	this.notifications = notifications;
	}
}