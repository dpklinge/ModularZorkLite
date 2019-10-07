package com.fdmgroup.zorkclone.actors.io;

import java.util.HashMap;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.actors.Actor;


public class InMemoryActorReader implements ActorReader {
	private static HashMap<String, Actor> actors = new HashMap<>();
	@Override
	public Actor readActor(String actorName) {
		if(actors.containsKey(actorName)){
			return actors.get(actorName);
		}else{
			ActorReader reader = new JsonActorReader(Main.baseGamePath);
			Actor actor = reader.readActor(actorName);
			actors.put(actorName, actor);
			return actor;
		}
	}

	@Override
	public synchronized Actor writeActor(Actor actor) {
		System.out.println("Adding actor to memory: "+actor);
		actors.put(actor.getName(), actor);
		return actor;
	}

	public static HashMap<String, Actor> getActors() {
		return actors;
	}

	@Override
	public Actor deleteActor(String actorName) {
		return actors.remove(actorName);
	}


	



}
