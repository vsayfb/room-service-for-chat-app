package com.example.room_service.repository;

import com.example.room_service.model.Room;
import com.example.room_service.repository.projection.RoomWithoutMembersProjection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends CrudRepository<Room, UUID> {

    List<RoomWithoutMembersProjection> findAllProjectBy();
}
