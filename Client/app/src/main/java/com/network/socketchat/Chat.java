package com.network.socketchat;

public class Chat {
    private int is_me;
    private String message;

    public Chat(int is_me, String message){
        this.is_me = is_me;
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public int getUser(){
        return is_me;
    }
}
