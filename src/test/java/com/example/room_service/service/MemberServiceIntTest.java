package com.example.room_service.service;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;
import com.example.room_service.repository.RoomRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberServiceIntTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void beforeEach() {
        roomRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Nested
    class CreateMember {

        @Test
        void shouldCreateMember() {

            Room room = new Room();

            room.setTitle("Title for Integration test");

            Room savedRoom = roomRepository.save(room);

            NewMemberDto member = new NewMemberDto();

            member.setUsername("walter");
            member.setUserId("123456");

            Member createdMember = memberService.createMember(member, savedRoom.getId());

            assertTrue(memberRepository.findById(new ObjectId(createdMember.getUserId())).isPresent());
        }

    }

    @Nested
    class DeleteMemberInChat {

        @Test
        void shouldDeleteMember() {
            Room room = new Room();

            room.setTitle("Title for integration test");

            roomRepository.save(room);

            Member member = new Member();

            member.setRoomId(room.getId());
            member.setUsername("walter");
            member.setUserId("123456");

            Member newMember = memberRepository.save(member);

            memberService.removeById(newMember.getId());

            assertTrue(memberRepository.findById(newMember.getId()).isEmpty());
        }

    }

}