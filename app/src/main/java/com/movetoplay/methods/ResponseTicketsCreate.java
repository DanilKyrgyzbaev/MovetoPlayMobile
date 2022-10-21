package com.movetoplay.methods;

import com.google.gson.annotations.SerializedName;

public class ResponseTicketsCreate{

	@SerializedName("ok")
	private boolean ok;

	public void setOk(boolean ok){
		this.ok = ok;
	}

	public boolean isOk(){
		return ok;
	}
}