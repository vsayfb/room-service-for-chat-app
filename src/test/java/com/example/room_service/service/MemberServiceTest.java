package com.example.room_service.service;

import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.exception.UserAlreadyInChatException;
import com.example.room_service.exception.UserNotInChatException;
import com.example.room_service.external.Client;
import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static  org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private MemberService memberService;

    Member member = new Member();

    Client client = new Client();

    @BeforeEach
    void beforeEach(){

        client.setUserId("2141535");
        client.setUsername("walter");

        member.setUserId(client.getUserId());
        member.setUsername(client.getUsername());
    }


    @Nested
    class CreateMember{

        @Test
        void shouldThrowRoomNotFoundException(){
            when(roomService.getById(any(ObjectId.class))).thenReturn(Optional.empty());

            assertThrows(RoomNotFoundException.class, () -> memberService.createMember(new Client(), ObjectId.get()));
        }

        @Test
        void shouldThrowUserAlreadyInChatException(){
            Client client = new Client();

            client.setUserId("12417284");
            client.setUsername("walter");

            when(roomService.getById(any(ObjectId.class))).thenReturn(Optional.of(new Room()));

            when(memberRepository.findByUserIdAndRoomId(any(String.class), any(ObjectId.class))).thenReturn(Optional.of(new Member()));

            assertThrows(UserAlreadyInChatException.class, () -> memberService.createMember(client, ObjectId.get()));
        }


        @Test
        void shouldCreateMember(){

            Room dummyRoom = new Room();

            dummyRoom.setId(ObjectId.get());

            when(roomService.getById(any(ObjectId.class))).thenReturn(Optional.of(dummyRoom));

            when(memberRepository.findByUserIdAndRoomId(any(String.class), any(ObjectId.class))).thenReturn(Optional.empty());

            Member dummyMember = new Member();

            dummyMember.setUserId(client.getUserId());
            dummyMember.setUsername(client.getUsername());
            dummyMember.setRoomId(dummyRoom.getId());

            when(memberRepository.save(any(Member.class))).thenReturn(dummyMember);

            Member created = memberService.createMember(client, dummyRoom.getId());

            assertEquals(created.getUsername(), client.getUsername());
            assertEquals(created.getUserId(), client.getUserId());
            assertEquals(created.getRoomId(), dummyRoom.getId());
        }

    }

    @Nested
    class DeleteMemberInChat{

        @Test
        void shouldThrowErrorIfUserNotInRoom(){
            when(memberRepository.findByUserIdAndRoomId(any(String.class), any(ObjectId.class))).thenReturn(Optional.empty());

           assertThrows(UserNotInChatException.class, () ->  memberService.deleteMemberInRoom(client, ObjectId.get()));
        }

        @Test
        void shouldThrowErrorIfUserInRoomButUserIdsNotMatched(){
            when(memberRepository.findByUserIdAndRoomId(any(String.class), any(ObjectId.class))).thenReturn(Optional.of(member));

            member.setUserId("14890894");

            assertThrows(UserNotInChatException.class, () ->  memberService.deleteMemberInRoom(client, ObjectId.get()));

        }

        @Test
        void shouldDeleteUserInChat(){
            when(memberRepository.findByUserIdAndRoomId(any(String.class), any(ObjectId.class))).thenReturn(Optional.of(member));

            memberService.deleteMemberInRoom(client, ObjectId.get());
        }
    }

}
