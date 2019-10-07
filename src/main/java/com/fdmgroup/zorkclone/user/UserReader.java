package com.fdmgroup.zorkclone.user;

public interface UserReader {

	public User create(User user);

	public User delete(User user);

	public User update(User user);

	public User read(String username);

	public boolean doesExist(User user);

}