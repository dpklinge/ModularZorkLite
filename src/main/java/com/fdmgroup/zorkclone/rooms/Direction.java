package com.fdmgroup.zorkclone.rooms;

public enum Direction {
	NORTH,SOUTH,EAST,WEST,NORTHEAST,SOUTHEAST,NORTHWEST,SOUTHWEST,UP,DOWN;
	public static String getEnumString(Direction direction){
		if(direction==NORTH){
			return "north";
		}else if(direction==SOUTH){
			return "south";
		}else if(direction==EAST){
			return "east";
		}else if(direction==WEST){
			return "west";
		}else if(direction==NORTHEAST){
			return "northeast";
		}else if(direction==SOUTHEAST){
			return "southeast";
		}else if(direction==NORTHWEST){
			return "northwest";
		}else if(direction==SOUTHWEST){
			return "southwest";
		}else if(direction==UP){
			return "up";
		}else if(direction==DOWN){
			return "down";
		}else{
			return "";
		}
		
	}
}
