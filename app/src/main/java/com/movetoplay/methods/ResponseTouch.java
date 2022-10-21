package com.movetoplay.methods;

import com.google.gson.annotations.SerializedName;

public class ResponseTouch{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("seconds")
	private int seconds;

	@SerializedName("profileId")
	private Object profileId;

	@SerializedName("count")
	private int count;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSeconds(int seconds){
		this.seconds = seconds;
	}

	public int getSeconds(){
		return seconds;
	}

	public void setProfileId(Object profileId){
		this.profileId = profileId;
	}

	public Object getProfileId(){
		return profileId;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}