package com.hermes;

import java.util.Date;

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
	
	public String getCreateDate()
	{
		return created_at.toString();
	}
	
	public String getUpdateDate()
	{
		return updated_at.toString();
	}
}
