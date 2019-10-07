package com.fdmgroup.zorkclone.actors.io;

import com.fdmgroup.zorkclone.actors.Actor;

public interface ActorReader {
	public Actor readActor(String actorName);
	public Actor writeActor(Actor actor);
	public Actor deleteActor(String actorName);

}
