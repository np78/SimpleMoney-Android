package com.hermes;

public class TransactionModel {
	private int id;
	private int recipient_id;
	private int sender_id;
	private String recipient_email;
	private String sender_email;
	private String description;
	private int amount;	
	private String currency;
	private String complete;
	private String created_at;
	private String updated_at;
	
	public int getID()
	{
		return id;
	}
	
	public int getRecipientID()
	{
		return recipient_id;
	}
	
	public int getSenderID()
	{
		return sender_id;
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
	
	public int getAmount()
	{
		return amount;
	}
	
	public String getAmountString()
	{
		if(amount%100 < 10)
			return "$" + amount/100 + ".0" + amount%100;
		return "$" + amount/100 + "." + amount%100;
	}
	
	public String getCurrency()
	{
		return currency;
	}
	
	public String getComplete()
	{
		return complete;
	}
	
	public String getCreateDate()
	{
		return created_at;
	}
	
	public String getUpdateDate()
	{
		return updated_at;
	}
}
