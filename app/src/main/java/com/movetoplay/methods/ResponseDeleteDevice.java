package com.movetoplay.methods;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseDeleteDevice{

	@SerializedName("ResponseDeleteDevice")
	private List<ResponseDeleteDeviceItem> responseDeleteDevice;

	public void setResponseDeleteDevice(List<ResponseDeleteDeviceItem> responseDeleteDevice){
		this.responseDeleteDevice = responseDeleteDevice;
	}

	public List<ResponseDeleteDeviceItem> getResponseDeleteDevice(){
		return responseDeleteDevice;
	}
}