package com.example.room_service.service;

import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.external.Client;
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
    void beforeEach(){
        memberRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Nested
    class CreateRoom{


        @Test
        void shouldCreateRoom(){

            Client client = new Client();
            client.setUsername("walter");
            client.setUserId("1247812");

            CreateRoomDto roomDto = new CreateRoomDto();
            roomDto.setTitle("Breaking Bad");

            Room created = roomService.createRoom(client, roomDto);

            assertTrue(roomRepository.findById(created.getId()).isPresent());

            Room room = roomRepository.findById(created.getId()).get();

            System.out.println("created ->" + room);

            assertEquals(room.getMembers().get(0).getUsername(), client.getUsername());
            assertEquals(room.getMembers().get(0).getUserId(), client.getUserId());
        }
    }
}
