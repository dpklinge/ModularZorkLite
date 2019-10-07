package com.fdmgroup.zorkclone.combat;

import java.util.List;

public interface Fightable {
	int getHealth();
	int getMaxHealth();
	int getDamage();
	int getAccuracy();
	int getDamageReduction();
	int getAccuracyReduction();
	void setHealth(int health);
	void setMaxHealth(int maxHealth);
	void setDamage(int damage);
	void setAccuracy(int accuracy);
	void setDamageReduction(int damageReduction );
	void setAccuracyReduction(int accuracyReduction);
	List<String> getAliases();
	boolean getIsDead();
	void setIsDead(boolean isDead);
	String getDisplayName();
	String getKillText();
}
