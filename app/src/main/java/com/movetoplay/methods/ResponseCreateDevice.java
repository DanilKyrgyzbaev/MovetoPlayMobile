package com.movetoplay.methods;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseCreateDevice{

	@SerializedName("ResponseCreateDevice")
	private List<ResponseCreateDeviceItem> responseCreateDevice;

	public void setResponseCreateDevice(List<ResponseCreateDeviceItem> responseCreateDevice){
		this.responseCreateDevice = responseCreateDevice;
	}

	public List<ResponseCreateDeviceItem> getResponseCreateDevice(){
		return responseCreateDevice;
	}
}