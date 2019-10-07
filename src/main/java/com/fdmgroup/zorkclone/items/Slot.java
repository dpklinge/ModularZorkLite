package com.fdmgroup.zorkclone.items;

public enum Slot {
	HEAD,BROW,CHEST,SHOULDER,ARM,GLOVE,LEG,BELT,RING,FOOT,CLOAK,NECKLACE,LEFTHAND,RIGHTHAND,TWOHANDS;
	
	public static String getEnumString(Slot slot){
		if(slot==HEAD){
			return "head";
		}else if(slot==BROW){
			return "brow";
		}else if(slot==CHEST){
			return "chest";
		}else if(slot==SHOULDER){
			return "shoulder";
		}else if(slot==ARM){
			return "arm";
		}else if(slot==GLOVE){
			return "glove";
		}else if(slot==LEG){
			return "leg";
		}else if(slot==BELT){
			return "belt";
		}else if(slot==RING){
			return "ring";
		}else if(slot==FOOT){
			return "feet";
		}else if(slot==CLOAK){
			return "cloak";
		}else if(slot==NECKLACE){
			return "necklace";
		}else if(slot==LEFTHAND){
			return "left hand";
		}else if(slot==RIGHTHAND){
			return "right hand";
		}else if(slot==TWOHANDS){
			return "both hands";
		}else{
			return "";
		}
		
	}
}
