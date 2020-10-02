package com.example.apoc.types;

import com.example.apoc.DB.DBItem;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * class represents a message object
 */
public class Message implements DBItem, Serializable {
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

    public Message(String msgContent, String msgWriter)
    {
        content = msgContent;
        writer = msgWriter;
        date = new Date();
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

    public String getFormatDate(){
        return date.toString();
    }

    public String getContent() {
        return content;
    }

    public String getWriter() {
        return writer;
    }

    @Override
    public String getId() {
        return writer + "_" + date.toString();
    }
}
