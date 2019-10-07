package com.fdmgroup.zorkclone.effects;

import java.util.List;

public interface Effectables {
	List<String> getEffects();
	List<String> getAliases();
	String getName();
	String examine();
	String getDisplayName();
}
