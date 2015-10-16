package com.morefriends.morefriends;

/**
 * Created by w23html on 10/16/15.
 */
public class User {
    String name = null;
    String email = null;
    String description = null;
    int age = 0;
    byte[] image = null;

    public User(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String des) {
        this.description = des;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDescription() {
        return this.description;
    }

    public int getAge() {
        return this.age;
    }

    public byte[] getImage() {
        return this.image;
    }
}
