package com.hermes;

import java.util.LinkedList;

public class UserModel {
	private int id;
	private String name;
	private String email;
	private String password;
	private int balance;
	private String currency;
	private String created_at;
	private String update_at;
	
	public int getID()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public String getBalance()
	{
		if(balance%100 < 10)
			return "$" + balance/100 + ".0" + balance%100;
		return "$" + balance/100 + "." + balance%100;
	}

	public String getCurrency()
	{
		return currency;
	}
	
	public String getCreatedAt()
	{
		return created_at;
	}
	
	public String getUpdateAt()
	{
		return update_at;
	}
}
