package com.morefriends.morefriends;

/**
 * Created by Cathy on 9/25/15.
 */
public class Contact {

    String name = "";
    String lastMessage = "";
    String date = "";
    String objectId = "";

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public Contact(String name, String lastMessage, String date){
        this.name = name;
        this.lastMessage = lastMessage;
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getDate() {
        return date;
    }

}
