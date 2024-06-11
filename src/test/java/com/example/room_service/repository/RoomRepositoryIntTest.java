package com.example.room_service.repository;

import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class RoomRepositoryIntTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    void testLookupProperty() {
        Room roomModel = new Room();

        roomModel.setTitle("Why Breaking Bad is the best show ever?");

        Room newRoom = roomRepository.save(roomModel);

        Member member = new Member();

        member.setUsername("walter");

        member.setRoomId(newRoom.getId());

        Member savedMember = memberRepository.save(member);

        assertEquals(roomRepository.findById(newRoom.getId()).get().getMembers().size(), 1);

        memberRepository.delete(savedMember);

        assertEquals(roomRepository.findById(newRoom.getId()).get().getMembers().size(), 0);
    }
}
