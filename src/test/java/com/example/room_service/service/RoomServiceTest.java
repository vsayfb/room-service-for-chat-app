package com.example.room_service.service;

import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.external.Client;
import com.example.room_service.model.Room;
import com.example.room_service.repository.RoomRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private  RoomRepository roomRepository;

    @Mock
    private  MemberService memberService;

    @InjectMocks
    private RoomService roomService;


    @Nested
    class CreateRoom{


        @Test
        void shouldCreateRoom(){

            Client client = new Client();
            client.setUsername("walter");
            client.setUserId("1247812");

            CreateRoomDto roomDto = new CreateRoomDto();
            roomDto.setTitle("Breaking Bad");

            Room room = new Room();
            room.setTitle(roomDto.getTitle());
            room.setId(ObjectId.get());

            when(roomRepository.save(any(Room.class))).thenReturn(room);

            Room created = roomService.createRoom(client, roomDto);

            assertInstanceOf(ObjectId.class, created.getId());
            assertEquals(created.getTitle(), room.getTitle());
        }
    }

}
