package com.example.room_service.repository;

import com.example.room_service.model.Room;
import com.example.room_service.repository.projection.RoomWithoutMembersProjection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RoomRepository extends MongoRepository<Room, ObjectId> {

    List<RoomWithoutMembersProjection> findAllProjectBy();
}
