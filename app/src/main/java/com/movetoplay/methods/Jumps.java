package com.movetoplay.methods;

import com.google.gson.annotations.SerializedName;

public class Jumps{

	@SerializedName("seconds")
	private int seconds;

	@SerializedName("count")
	private int count;

	public void setSeconds(int seconds){
		this.seconds = seconds;
	}

	public int getSeconds(){
		return seconds;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}
}