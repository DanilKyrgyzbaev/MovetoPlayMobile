package com.movetoplay.methods;

import com.google.gson.annotations.SerializedName;

public class ParentAccount{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("password")
	private String password;

	@SerializedName("role")
	private String role;

	@SerializedName("isBanned")
	private boolean isBanned;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	@SerializedName("age")
	private int age;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setRole(String role){
		this.role = role;
	}

	public String getRole(){
		return role;
	}

	public void setIsBanned(boolean isBanned){
		this.isBanned = isBanned;
	}

	public boolean isIsBanned(){
		return isBanned;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setAge(int age){
		this.age = age;
	}

	public int getAge(){
		return age;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}