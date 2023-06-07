//package com.fdmgroup.zorkclone.effects.effectio;
//
//import com.fdmgroup.zorkclone.effects.Effect;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EffectClassReader implements EffectReader {
//
//	@Override
//	public Effect readEffect(String effectName) {
//		try {
//			Class<?> effect = Class.forName("com.fdmgroup.zorkclone.effects."+effectName);
//			Effect resolvedEffect = (Effect) effect.newInstance();
//			return resolvedEffect;
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//			return null;
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//
//	}
//
//
//}
