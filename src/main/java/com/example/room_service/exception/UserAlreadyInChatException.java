package com.example.room_service.exception;

public class UserAlreadyInChatException extends RuntimeException{

    public UserAlreadyInChatException(String username){
        super(String.format("User %s is already a member of this chat.", username));
    }

}
