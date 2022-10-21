package com.movetoplay.methods;

import com.google.gson.annotations.SerializedName;

public class ResponseGetInfoC{

	@SerializedName("parentAccountId")
	private String parentAccountId;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("sex")
	private String sex;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("id")
	private String id;

	@SerializedName("parentAccount")
	private ParentAccount parentAccount;

	@SerializedName("age")
	private int age;

	@SerializedName("isEnjoySport")
	private boolean isEnjoySport;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setParentAccountId(String parentAccountId){
		this.parentAccountId = parentAccountId;
	}

	public String getParentAccountId(){
		return parentAccountId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSex(String sex){
		this.sex = sex;
	}

	public String getSex(){
		return sex;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setParentAccount(ParentAccount parentAccount){
		this.parentAccount = parentAccount;
	}

	public ParentAccount getParentAccount(){
		return parentAccount;
	}

	public void setAge(int age){
		this.age = age;
	}

	public int getAge(){
		return age;
	}

	public void setIsEnjoySport(boolean isEnjoySport){
		this.isEnjoySport = isEnjoySport;
	}

	public boolean isIsEnjoySport(){
		return isEnjoySport;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}