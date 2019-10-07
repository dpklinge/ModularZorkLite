package com.fdmgroup.zorkclone.actors.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.zorkclone.actors.Actor;

public class JsonAllActorReader implements AllActorReader {

	private ObjectMapper mapper = new ObjectMapper();
	{
	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	private Path storagePath;
	
	public JsonAllActorReader(Path storagePath) {
		super();
		this.storagePath = storagePath;
	}
	
	@Override
	public List<Actor> readAllActors() {
		List<Actor> actors= new ArrayList<>();
		try {
			File dir = new File(storagePath+"\\actors\\");
			  File[] directoryListing = dir.listFiles();
			  if (directoryListing != null) {
			    for (File child : directoryListing) {
			      Actor actor = mapper.readValue(child, Actor.class);
			      actors.add(actor);
			    }
			  } else {
			    System.out.println("Actor readall failed to find directory "+dir.getAbsolutePath());
			  }
	
		} catch (JsonParseException e) {
			System.err.println("Json read failed");
		} catch (JsonMappingException e) {
			System.err.println("Json read failed");
		} catch (IOException e) {
			System.err.println("Actor read all failure");
			e.printStackTrace();
		}
		return actors;
	}

}
