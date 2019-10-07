package com.fdmgroup.zorkclone.commands;

public class CommandParser {

	public String validateFirstCommand(String[] commands) {
		if(commands.length==0||commands[0].equals("")){
			return "";
		}
		String firstCommand=commands[0];
		try{
			Commands.valueOf(firstCommand);
		}catch(Exception e){
			return "unrecognized command";
		}
		return firstCommand;
	}

}
