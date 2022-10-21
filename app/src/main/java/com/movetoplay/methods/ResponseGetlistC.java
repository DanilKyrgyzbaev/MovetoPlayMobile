package com.movetoplay.methods;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseGetlistC{

	@SerializedName("ResponseGetlistC")
	private List<ResponseGetlistCItem> responseGetlistC;

	public void setResponseGetlistC(List<ResponseGetlistCItem> responseGetlistC){
		this.responseGetlistC = responseGetlistC;
	}

	public List<ResponseGetlistCItem> getResponseGetlistC(){
		return responseGetlistC;
	}
}