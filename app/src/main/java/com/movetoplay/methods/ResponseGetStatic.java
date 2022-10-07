package com.movetoplay.methods;

import com.google.gson.annotations.SerializedName;

public class ResponseGetStatic{

	@SerializedName("squeezing")
	private Squeezing squeezing;

	@SerializedName("squats")
	private Squats squats;

	@SerializedName("jumps")
	private Jumps jumps;

	public void setSqueezing(Squeezing squeezing){
		this.squeezing = squeezing;
	}

	public Squeezing getSqueezing(){
		return squeezing;
	}

	public void setSquats(Squats squats){
		this.squats = squats;
	}

	public Squats getSquats(){
		return squats;
	}

	public void setJumps(Jumps jumps){
		this.jumps = jumps;
	}

	public Jumps getJumps(){
		return jumps;
	}
}