package com.example.room_service.repository;

import com.example.room_service.model.Member;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, ObjectId> {

    Optional<Member> findByRoomId(ObjectId roomId);
}
