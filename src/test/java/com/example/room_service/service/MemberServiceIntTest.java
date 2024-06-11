package com.example.room_service.service;

import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.exception.UserAlreadyInChatException;
import com.example.room_service.exception.UserNotInChatException;
import com.example.room_service.external.Client;
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


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


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
        void shouldThrowRoomNotFoundException(){
            assertThrows(RoomNotFoundException.class, () -> memberService.createMember(new Client(), ObjectId.get()));
        }

        @Test
        void shouldThrowUserAlreadyInChatException(){

            Room room = new Room();
            room.setTitle("Breaking bad");

            Room savedRoom = roomRepository.save(room);

            Client client = new Client();
            client.setUserId("12417284");
            client.setUsername("walter");

            Member member = new Member();
            member.setUsername(client.getUsername());
            member.setUserId(client.getUserId());
            member.setRoomId(savedRoom.getId());

            memberRepository.save(member);

            assertThrows(UserAlreadyInChatException.class, () -> memberService.createMember(client, savedRoom.getId()));
        }


        @Test
        void shouldCreateMember() {

            Room room = new Room();

            room.setTitle("Title for Integration test");

            Room savedRoom = roomRepository.save(room);

            Client client = new Client();

            client.setUsername("walter");
            client.setUserId("123456");

            Member createdMember = memberService.createMember(client, savedRoom.getId());

            assertTrue(roomRepository.findById(savedRoom.getId()).isPresent());

            assertEquals(roomRepository.findById(savedRoom.getId()).get().getMembers().get(0).getId(), createdMember.getId());
        }

    }

    @Nested
    class DeleteMemberInChat {

        @Test
        void shouldThrowErrorIfUserNotInRoom() {
            Room room = new Room();

            room.setTitle("Title for integration test");

            Room savedRoom = roomRepository.save(room);

            Client client = new Client();

            client.setUsername("walter");
            client.setUserId("123456");

            assertThrows(UserNotInChatException.class, () -> memberService.deleteMemberInRoom(client, savedRoom.getId()));
        }

        @Test
        void shouldThrowErrorIfAUserInRoomButUserIdsNotMatched() {
            Room room = new Room();

            room.setTitle("Title for integration test");

            Room savedRoom = roomRepository.save(room);

            Client walter = new Client();

            walter.setUsername("walter");
            walter.setUserId("123456");

            Member memberWalter = new Member();

            memberWalter.setRoomId(savedRoom.getId());
            memberWalter.setUsername(walter.getUsername());
            memberWalter.setUserId(walter.getUserId());

            memberRepository.save(memberWalter);

            Client jessy = new Client();

            jessy.setUsername("jessy");
            jessy.setUserId("789012");

            assertThrows(UserNotInChatException.class, () -> memberService.deleteMemberInRoom(jessy, savedRoom.getId()));
        }

        @Test
        void shouldDeleteUserInChat() {
            Room room = new Room();

            room.setTitle("Title for integration test");

            Room savedRoom = roomRepository.save(room);

            Client walter = new Client();

            walter.setUsername("walter");
            walter.setUserId("123456");

            Member memberWalter = new Member();

            memberWalter.setRoomId(savedRoom.getId());
            memberWalter.setUsername(walter.getUsername());
            memberWalter.setUserId(walter.getUserId());

            memberRepository.save(memberWalter);

            assertTrue(roomRepository.findById(savedRoom.getId()).isPresent());

            assertEquals(1, roomRepository.findById(savedRoom.getId()).get().getMembers().size());

            memberService.deleteMemberInRoom(walter, room.getId());

            assertTrue(roomRepository.findById(savedRoom.getId()).get().getMembers().isEmpty());
        }

    }

}