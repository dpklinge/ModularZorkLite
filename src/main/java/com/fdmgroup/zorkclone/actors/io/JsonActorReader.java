package com.fdmgroup.zorkclone.actors.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.zorkclone.actors.Actor;

public class JsonActorReader implements ActorReader {

	private ObjectMapper mapper = new ObjectMapper();
	{
	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	private Path storagePath;
	
	public JsonActorReader(Path storagePath) {
		super();
		this.storagePath = storagePath;
	}

	public void setPath(Path path) {
		this.storagePath = path;
		
	}
	
	@Override
	public Actor readActor(String actorId) {
		
		Actor actor= null;
		try {
			actor = mapper.readValue(new File((storagePath+"\\actors\\"+actorId+".txt")), Actor.class);
		} catch (JsonParseException e) {
			System.err.println("Json read failed");
		} catch (JsonMappingException e) {
			System.err.println("Json read failed");
		} catch (IOException e) {
			System.err.println("Tried to read from file path: "+(storagePath+"\\actors\\"+actorId+".txt"));
		}
		return actor;
	}

	@Override
	public  Actor writeActor(Actor actor) {
		System.out.println("Attempting to write actor: "+actor);
		System.out.println("Writing to: "+new File(storagePath+"\\actors\\"+actor.getName()+".txt"));
		
		try {
			mapper.writeValue(new File(storagePath+"\\actors\\"+actor.getName()+".txt"),actor);
		} catch (JsonGenerationException e) {
			System.err.println("JSON failed");
			
		} catch (JsonMappingException e) {
			System.err.println("JSON failed");
			
		} catch (IOException e) {
			System.err.println("File path "+storagePath+"\\actors\\"+actor.getName()+" not found!");
		}
		return actor;
	}

	@Override
	public Actor deleteActor(String actorName) {
		// TODO
		return null;
	}

	

}
