package com.movetoplay.methods;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseAuthProfile{

	@SerializedName("ResponseAuthProfile")
	private List<ResponseAuthProfileItem> responseAuthProfile;

	public void setResponseAuthProfile(List<ResponseAuthProfileItem> responseAuthProfile){
		this.responseAuthProfile = responseAuthProfile;
	}

	public List<ResponseAuthProfileItem> getResponseAuthProfile(){
		return responseAuthProfile;
	}
}