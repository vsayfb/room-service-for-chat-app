package com.example.room_service.service;

import com.example.room_service.exception.UserNotInChatException;
import com.example.room_service.external.Client;
import com.example.room_service.model.Member;
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
        void shouldCreateMember(){
            ObjectId id = ObjectId.get();

            member.setId(id);

            when(memberRepository.save(any(Member.class))).thenReturn(member);

            Member created = memberService.createMember(client, id);

            assertEquals(created.getId(), member.getId());
            assertEquals(created.getUsername(), client.getUsername());
            assertEquals(created.getUserId(), client.getUserId());
        }

    }

    @Nested
    class DeleteMemberInChat{

        @Test
        void shouldThrowErrorIfUserNotInRoom(){
            when(memberRepository.findByRoomId(any(ObjectId.class))).thenReturn(Optional.empty());

           assertThrows(UserNotInChatException.class, () ->  memberService.deleteMemberInRoom(client, ObjectId.get()));
        }

        @Test
        void shouldThrowErrorIfUserInRoomButUserIdsNotMatched(){
            when(memberRepository.findByRoomId(any(ObjectId.class))).thenReturn(Optional.of(member));

            member.setUserId("14890894");

            assertThrows(UserNotInChatException.class, () ->  memberService.deleteMemberInRoom(client, ObjectId.get()));

        }

        @Test
        void shouldDeleteUserInChat(){
            when(memberRepository.findByRoomId(any(ObjectId.class))).thenReturn(Optional.of(member));

            memberService.deleteMemberInRoom(client, ObjectId.get());
        }
    }

}
