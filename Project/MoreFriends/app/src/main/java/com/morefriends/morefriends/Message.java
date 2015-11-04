package com.morefriends.morefriends;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by w23html on 11/4/15.
 */
@ParseClassName("Message")
public class Message extends ParseObject {

    public Message() {}

    public String getUserId() {
        return getString("userId");
    }

    // Get the message from parse
    public String getBody() {
        return getString("body");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    // Put the message to parse
    public void setBody(String body) {
        put("body", body);
    }

}
