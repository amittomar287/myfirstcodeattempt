package com.example.geofenceapp;

import java.io.Serializable;

public class BeanClass implements Serializable{
	
	public String address_name;
	public String address;
	public String address_duration;
	public String address_icon;
	public String address_radius;
	
	public String address_entryTime;
	public String address_TimeDuration;
	
	public BeanClass() {
		super();
	}

	public BeanClass(String address_name, String address, String address_icon,String address_radius) {
		super();
		this.address_name = address_name;
		this.address = address;
		this.address_icon = address_icon;
		this.address_radius = address_radius;
	}
	
	public BeanClass(String address_name, String address, String address_icon,String address_radius,String address_entryTime,String address_TimeDuration) {
		super();
		this.address_name = address_name;
		this.address = address;
		this.address_icon = address_icon;
		this.address_radius = address_radius;
		this.address_entryTime=address_entryTime;
		this.address_TimeDuration=address_TimeDuration;
	}

	public BeanClass(String address_name, String address,String address_duration, String address_icon, String address_radius) {
		super();
		this.address_name = address_name;
		this.address = address;
		this.address_duration = address_duration;
		this.address_icon = address_icon;
		this.address_radius = address_radius;
	}

	public String getAddress_name() {
		return address_name;
	}

	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress_duration() {
		return address_duration;
	}

	public void setAddress_duration(String address_duration) {
		this.address_duration = address_duration;
	}

	public String getAddress_icon() {
		return address_icon;
	}

	public void setAddress_icon(String address_icon) {
		this.address_icon = address_icon;
	}

	public String getAddress_radius() {
		return address_radius;
	}

	public void setAddress_radius(String address_radius) {
		this.address_radius = address_radius;
	}

	

}
