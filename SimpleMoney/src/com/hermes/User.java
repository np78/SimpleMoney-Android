package com.hermes;

//User model used to read GSON data into object
//Only had getter methods
public class User {
	private int id;
	private String avatar_url;
	private String avatar_url_small;
	private String name;
	private String email;
	private int balance;
	private Transaction[] transactions;
	
	public String getAvatarURL()
	{
		return avatar_url;
	}
	
	public String getAvatarURLSmall()
	{
		return avatar_url_small;
	}
	
	public Transaction[] getTransactions()
	{
		return transactions;
	}
	
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
	
	//Formats the balance string
	public String getBalance()
	{
		if(balance%100 < 10)
			return "$" + balance/100 + ".0" + balance%100;
		return "$" + balance/100 + "." + balance%100;
	}
}
