package com.example.room_service.repository;

import com.example.room_service.model.Room;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, ObjectId> {
}
