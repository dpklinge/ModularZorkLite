package com.fdmgroup.zorkclone.user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.zorkclone.Main;

public class JsonUserReader implements UserReader {

	private ObjectMapper mapper = new ObjectMapper();
	{
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	private Path storagePath;

	public JsonUserReader() {
		this.storagePath = Main.userPath;
	}

	@Override
	public User create(User user) {
		System.out.println("Creating user: "+storagePath +"/"+ user.getUsername() +"/"+ user.getUsername()+ ".txt");
		File file = new File(storagePath +"/"+ user.getUsername() +"/"+ user.getUsername()+ ".txt");
		if (file.exists()) {
			return null;
		} else {
			try {
				Files.createDirectories(Paths.get(file.toPath().getParent().toString(),"/characters/"));
				
				mapper.writeValue(file, user);
				System.out.println("Created user: "+storagePath +"/"+ user.getUsername() +"/"+ user.getUsername()+ ".txt");
			} catch (JsonGenerationException e) {
				System.err.println("JSON failed");
				e.printStackTrace();
			} catch (JsonMappingException e) {
				System.err.println("JSON failed");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("File path " + storagePath +"/"+ user.getUsername() +"/"+ user.getUsername()+ ".txt"+" not found!");
				e.printStackTrace();
			}
		}
		return user;

	}

	@Override
	public User delete(User user) {
		File file = new File(storagePath +"/"+user.getUsername() +"/"+ user.getUsername()+ ".txt");
		if (file.exists()) {
			file.delete();
			return user;
		} else {
			return null;
		}

	}

	@Override
	public User update(User user) {
		File file = new File(storagePath +"/"+ user.getUsername() +"/"+ user.getUsername()+ ".txt");		try {
			mapper.writeValue(file, user);
		} catch (JsonGenerationException e) {
			System.err.println("JSON failed");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.err.println("JSON failed");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File path " + storagePath +"/"+ user.getUsername() +"/"+ user.getUsername()+ ".txt"+" not found!");
			e.printStackTrace();
		}
		return user;

	}

	@Override
	public User read(String username) {
		for(File file : new File(storagePath+"/").listFiles()){
			if(file.getName().equalsIgnoreCase(username)){
				User user= null;
				try {
					user = mapper.readValue(new File(storagePath +"/"+ file.getName() +"/" + file.getName() + ".txt"), User.class);
					return user;
				} catch (JsonParseException e) {
					System.err.println("Json read failed");
					e.printStackTrace();
				} catch (JsonMappingException e) {
					System.err.println("Json read failed");
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Tried to read from file path: "+(storagePath  +"/"+ username +"/"+ username+ ".txt"));
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

	@Override
	public boolean doesExist(User user) {
		File file = new File(storagePath +"/");
		for(File subFile : file.listFiles()){
			if(subFile.getName().equalsIgnoreCase(user.getUsername())){
				return true;
			}
		}
		
		return false;
	}

}
