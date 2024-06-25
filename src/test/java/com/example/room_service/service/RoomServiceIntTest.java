package com.example.room_service.service;

import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;
import com.example.room_service.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomServiceIntTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Nested
    class CreateRoom {


        @Test
        void shouldCreateRoomAndAddMember() {

            NewMemberDto newMemberDto = new NewMemberDto();
            newMemberDto.setUsername("walter");
            newMemberDto.setUserId("1247812");

            CreateRoomDto roomDto = new CreateRoomDto();

            roomDto.setTitle("Breaking Bad");

            Room created = roomService.createRoom(newMemberDto, roomDto);

            assertTrue(roomRepository.findById(created.getId()).isPresent());

            Room room = roomRepository.findById(created.getId()).get();

            assertEquals(created.getTitle(), room.getTitle());

            assertTrue(memberRepository.findByUserId(newMemberDto.getUserId()).isPresent());

        }
    }
}
