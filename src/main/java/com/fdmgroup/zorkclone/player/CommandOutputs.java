package com.fdmgroup.zorkclone.player;

import java.util.ArrayList;
import java.util.List;

public class CommandOutputs {
	private List<String> outputs = new ArrayList<>();
	private List<String> chatOutputs = new ArrayList<>();

	public List<String> getChatOutputs() {
		return chatOutputs;
	}

	public void setChatOutputs(List<String> chatOutputs) {
		this.chatOutputs = chatOutputs;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<String> outputs) {
		this.outputs = outputs;
	}
	
	public String displayOutputsWeb(){
		String output="";
		for(String string:outputs){
			output=output+string +"<br />";
		}
		return output;
	}

	public String displayChatOuputs() {
		String output="";
		for(String string:chatOutputs){
			output=output+string +"<br />";
		}
		return output;
	}

	@Override
	public String toString() {
		return "CommandOutputs [outputs=" + outputs + ", chatOutputs=" + chatOutputs + "]";
	}
	

}
