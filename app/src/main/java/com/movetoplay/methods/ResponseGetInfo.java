package com.movetoplay.methods;

import com.google.gson.annotations.SerializedName;

public class ResponseGetInfo{

	@SerializedName("message")
	private String message;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}