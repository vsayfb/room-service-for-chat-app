package com.example.room_service.service;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.exception.UserAlreadyInChatException;
import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;
import com.example.room_service.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

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

        void shouldThrowErrorIfRoomNotExists() {

            NewMemberDto member = new NewMemberDto();

            member.setUsername("walter");
            member.setUserId("123456");

            assertThrows(RoomNotFoundException.class, () -> memberService.createMember(member, UUID.randomUUID()));
        }

        @Test
        void shouldUpdatSessions() {

            Room room = new Room();

            room.setTitle("Title for Integration test");

            Room savedRoom = roomRepository.save(room);

            Member member = new Member();

            String sessionId = "1";

            member.setRoomId(savedRoom.getId());
            member.setUsername("walter");
            member.setUserId("123456");
            member.addSessionId(sessionId);

            memberRepository.save(member);

            NewMemberDto newMemberDto = new NewMemberDto();

            newMemberDto.setUserId(member.getUserId());
            newMemberDto.setUsername(member.getUsername());
            newMemberDto.setSessionId("2");

            Member updated = memberService.createMember(newMemberDto, savedRoom.getId());

            assertTrue(updated.getSessionIds().contains(sessionId));
            assertTrue(updated.getSessionIds().contains("2"));

        }

        @Test
        void shouldThrowExceptionIfSessionAlreadyExists() {

            Room room = new Room();

            room.setTitle("Title for Integration test");

            Room savedRoom = roomRepository.save(room);

            Member member = new Member();

            String sessionId = "1";

            member.setRoomId(savedRoom.getId());
            member.setUsername("walter");
            member.setUserId("123456");
            member.addSessionId(sessionId);

            memberRepository.save(member);

            NewMemberDto newMemberDto = new NewMemberDto();

            newMemberDto.setUserId(member.getUserId());
            newMemberDto.setUsername(member.getUsername());
            newMemberDto.setSessionId(sessionId);

            assertThrows(UserAlreadyInChatException.class,
                    () -> memberService.createMember(newMemberDto, savedRoom.getId()));

        }

        @Test
        void shouldSaveMember() {

            Room room = new Room();

            room.setTitle("Title for Integration test");

            Room savedRoom = roomRepository.save(room);

            NewMemberDto member = new NewMemberDto();

            String sessionId = "1";

            member.setUsername("walter");
            member.setUserId("123456");
            member.setSessionId(sessionId);

            Member createdMember = memberService.createMember(member, savedRoom.getId());

            assertTrue(memberRepository.findById(createdMember.getId()).isPresent());

            assertTrue(createdMember.getSessionIds().contains(sessionId));
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
            member.addSessionId("1");
            member.setUserId("123456");

            Member newMember = memberRepository.save(member);

            memberService.removeById(newMember.getId(), "1");

            assertTrue(memberRepository.findById(newMember.getId()).isEmpty());
        }

        @Test
        void shouldDeleteSessionId() {
            Room room = new Room();

            room.setTitle("Title for integration test");

            roomRepository.save(room);

            Member member = new Member();

            member.setRoomId(room.getId());
            member.setUsername("walter");
            member.addSessionId("1");
            member.addSessionId("2");
            member.setUserId("123456");

            Member newMember = memberRepository.save(member);

            memberService.removeById(newMember.getId(), "1");

            Optional<Member> found = memberRepository.findById(newMember.getId());

            assertTrue(found.isPresent());
            assertFalse(found.get().getSessionIds().contains("1"));
            assertTrue(found.get().getSessionIds().contains("2"));
        }

    }

}