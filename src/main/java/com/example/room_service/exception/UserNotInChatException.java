package com.example.room_service.exception;

public class UserNotInChatException extends RuntimeException{

    public UserNotInChatException(String username){
        super(String.format("User %s does not belong to this chat.", username));
    }
}
