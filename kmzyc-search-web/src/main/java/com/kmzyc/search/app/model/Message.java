package com.kmzyc.search.app.model;

import java.util.Map;

import com.kmzyc.search.app.jms.MessageQueue;

public class Message
{

	private MessageQueue		queue;
	private Map<String, Object>	msgData;

	public Message()
	{
	}

	public Message(MessageQueue queue, Map<String, Object> msgData)
	{
		this.setQueue(queue);
		this.msgData = msgData;
	}

	public Map<String, Object> getMsgData()
	{
		return msgData;
	}

	public void setMsgData(Map<String, Object> msgData)
	{
		this.msgData = msgData;
	}

	public MessageQueue getQueue()
	{
		return queue;
	}

	public void setQueue(MessageQueue queue)
	{
		this.queue = queue;
	}

}
