package com.movetoplay.methods;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseAuthProfileItem{

	@SerializedName("property")
	private String property;

	@SerializedName("messages")
	private List<String> messages;

	public void setProperty(String property){
		this.property = property;
	}

	public String getProperty(){
		return property;
	}

	public void setMessages(List<String> messages){
		this.messages = messages;
	}

	public List<String> getMessages(){
		return messages;
	}
}