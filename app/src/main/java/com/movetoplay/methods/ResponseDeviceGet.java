package com.movetoplay.methods;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseDeviceGet{

	@SerializedName("ResponseDeviceGet")
	private List<ResponseDeviceGetItem> responseDeviceGet;

	public void setResponseDeviceGet(List<ResponseDeviceGetItem> responseDeviceGet){
		this.responseDeviceGet = responseDeviceGet;
	}

	public List<ResponseDeviceGetItem> getResponseDeviceGet(){
		return responseDeviceGet;
	}
}