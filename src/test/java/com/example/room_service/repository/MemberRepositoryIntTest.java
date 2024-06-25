package com.example.room_service.repository;

import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class MemberRepositoryIntTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void beforeEach(){
        memberRepository.deleteAll();
    }

    @Test
    void testFindByUsername(){

        Member member = new Member();

        member.setUserId("1");

        Member savedMember = memberRepository.save(member);

        assertEquals(memberRepository.findByUserId("gus").get().getUsername(), "1");

        memberRepository.delete(savedMember);

        assertTrue(memberRepository.findByUserId("1").isEmpty());
    }

    @Test
    void testFindByRoomId(){

        Room room = new Room();

        room.setTitle("a title for room");

        Room savedRoom = roomRepository.save(room);

        Member member = new Member();

        member.setUsername("gus");

        member.setRoomId(savedRoom.getId());

        Member savedMember = memberRepository.save(member);

        assertTrue(memberRepository.findByRoomId(savedRoom.getId()).isPresent());

        assertEquals(memberRepository.findByRoomId(savedRoom.getId()).get().getId(), savedMember.getId());
    }
}
