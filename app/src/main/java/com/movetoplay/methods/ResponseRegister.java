package com.movetoplay.methods;

import com.google.gson.annotations.SerializedName;

public class ResponseRegister{

	@SerializedName("token")
	private String token;

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}