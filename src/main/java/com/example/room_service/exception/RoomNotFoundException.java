package com.example.room_service.exception;

public class RoomNotFoundException extends RuntimeException{

    public RoomNotFoundException(){
        super("Room not found.");
    }

}
