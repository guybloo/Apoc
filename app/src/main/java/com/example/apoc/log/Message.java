package com.example.apoc.log;

import java.sql.Time;
import java.util.Date;

class Message {
    private String content;
    private String writer;
    private Date date;

    public Message(String msgContent, String msgWriter, Date msgDate)
    {
        content = msgContent;
        writer = msgWriter;
        date = msgDate;
    }
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
