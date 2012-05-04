package com.hermes;

import java.util.Date;
import java.util.StringTokenizer;

//Transaction model used to read GSON data into object
//Only had getter methods
public class Transaction {
	private int id;
	private String recipient_email;
	private int recipient_id;
	private String sender_email;
	private int sender_id;
	private String description;
	private int amount;	
	private boolean complete;
	private Date created_at;
	private Date updated_at;
	private User recipient, sender;
	
	public User getRecipient()
	{
		return recipient;
	}
	
	public int getRecipientID()
	{
		return recipient_id;
	}
	
	public User getSender()
	{
		return sender;
	}
	
	public int getSenderID()
	{
		return sender_id;
	}
	
	public int getID()
	{
		return id;
	}
	
	public String getRecipientEmail()
	{
		return recipient_email;
	}
	
	public String getSenderEmail()
	{
		return sender_email;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	//Formats the balance string
	public String getAmount()
	{
		if(amount%100 < 10)
			return "$" + amount/100 + ".0" + amount%100;
		return "$" + amount/100 + "." + amount%100;
	}
	
	public boolean getComplete()
	{
		return complete;
	}
	
	//Formats the date string
	public String getCreateDate()
	{
		StringTokenizer st = new StringTokenizer(created_at.toString());
		String dayOfWeek = st.nextToken();
		String month = st.nextToken();
		String day = st.nextToken();
		String time = st.nextToken();
		String timezone = st.nextToken();
		String year = st.nextToken();
		return month + " " + day + ", " + year + " - " + time;
	}
	
	//Formats the date string
	public String getUpdateDate()
	{
		StringTokenizer st = new StringTokenizer(updated_at.toString());
		String dayOfWeek = st.nextToken();
		String month = st.nextToken();
		String day = st.nextToken();
		String time = st.nextToken();
		String timezone = st.nextToken();
		String year = st.nextToken();
		return month + " " + day + ", " + year + " - " + time;
	}
}
