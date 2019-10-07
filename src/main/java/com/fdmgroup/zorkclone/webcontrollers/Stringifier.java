package com.fdmgroup.zorkclone.webcontrollers;

import java.util.List;
import java.util.Map;

import com.fdmgroup.zorkclone.items.Item;
import com.fdmgroup.zorkclone.rooms.Direction;

public class Stringifier {
	public static String stringifyAliases(List<String> aliases) {
		String string = "";
		for(String alias : aliases){
			string+=alias+",";
		}
		return string;
	}

	public static String stringifyActors(List<String> list) {
		String string = "";
		for(String actor : list){
			string+=actor+",";
		}
		return string;
	}

	public static String stringifyRooms(Map<Direction, String> map) {
		String string = "";
		for(String room : map.values()){
			string+=room+",";
		}
		return string;
	}

	public static <T> String stringifyItems(List<T> list) {
		String string = "";
		for(Object item : list){
			if(item instanceof String){
			string+=item+",";
			}else if(item instanceof Item){
				string+=((Item)item).getName()+",";
			}
		}
		return string;
	}


}
