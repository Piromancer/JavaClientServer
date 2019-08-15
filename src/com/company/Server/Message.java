package com.company.Server;

public class Message {
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


    private String owner;
    private String text;
    private Command command;

    public Message(String owner, String text, Command command){
            this.owner = owner;
            this.text = text;
            this.command = command;
    }

    public Message(){};
}
