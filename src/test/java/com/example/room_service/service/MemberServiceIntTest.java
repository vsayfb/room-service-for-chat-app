package com.example.room_service.service;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.exception.RoomNotFoundException;
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

import java.util.Date;

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
        void shouldThrowErrorIfRoomNotExists() {

            NewMemberDto member = new NewMemberDto();

            member.setUsername("walter");
            member.setUserId("123456");

            assertThrows(RoomNotFoundException.class, () -> memberService.createMember(member, ObjectId.get()));
        }

        @Test
        void shouldCreateMember() {

            Room room = new Room();

            room.setTitle("Title for Integration test");

            Room savedRoom = roomRepository.save(room);

            NewMemberDto member = new NewMemberDto();

            member.setUsername("walter");
            member.setUserId("123456");

            Member createdMember = memberService.createMember(member, savedRoom.getId());

            assertTrue(memberRepository.findById(createdMember.getId()).isPresent());
        }

        @Test
        void shouldOnlyUpdateJoinDate() {

            Room room = new Room();

            room.setTitle("Title for Integration test");

            Room savedRoom = roomRepository.save(room);

            NewMemberDto member = new NewMemberDto();

            member.setUsername("walter");
            member.setUserId("123456");

            Member createdMember = memberService.createMember(member, savedRoom.getId());

            assertTrue(memberRepository.findById(createdMember.getId()).isPresent());

            Date joinedAt = createdMember.getJoinedAt();

            Member updatedMember = memberService.createMember(member, savedRoom.getId());

            Date joinedAt2 = updatedMember.getJoinedAt();

            Member foundMember = memberRepository.findById(createdMember.getId()).get();

            assertEquals(foundMember.getUsername(), createdMember.getUsername());
            assertEquals(foundMember.getUsername(), updatedMember.getUsername());

            assertNotEquals(joinedAt.getTime(), joinedAt2.getTime());
            assertNotEquals(joinedAt, joinedAt2);
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