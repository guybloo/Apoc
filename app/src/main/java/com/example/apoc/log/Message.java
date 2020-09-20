package com.example.apoc.log;

import java.sql.Time;
import java.util.Date;

/**
 * class represents a message object
 */
class Message {
    private String content;
    private String writer;
    private Date date;

    /**
     * constructor
     * @param msgContent the message itself
     * @param msgWriter who wrote it
     * @param msgDate when was is written
     */
    public Message(String msgContent, String msgWriter, Date msgDate)
    {
        content = msgContent;
        writer = msgWriter;
        date = msgDate;
    }

    /**
     * empty constructor
     */
    public Message(){
        this("","", new Date());
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getWriter() {
        return writer;
    }
}
