package com.postpc.apoc.types;

import com.postpc.apoc.DB.DBItem;

import java.io.Serializable;
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

    /**
     * constructor
     * @param msgContent the message
     * @param msgWriter the writer
     */
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

    /**
     * gets the date
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * gets formated date
     * @return
     */
    public String getFormatDate(){
        return date.toString();
    }

    /**
     * gets message content
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * gets message writer
     * @return
     */
    public String getWriter() {
        return writer;
    }

    /**
     * gets message id
     * @return
     */
    @Override
    public String getId() {
        return writer + "_" + date.toString();
    }
}
