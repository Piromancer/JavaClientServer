package com.company.Server;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message {
    private String owner;
    private String text;
    private Command command;
    private String creationTime;

    public Message(String owner, String text, Command command){
        this.owner = owner;
        this.text = text;
        this.command = command;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"); //20 symbols with a space
        creationTime = sdf.format(Calendar.getInstance().getTime());
    }

    public Message(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    };


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
