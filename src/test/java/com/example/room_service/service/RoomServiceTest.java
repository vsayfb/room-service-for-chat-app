package com.example.room_service.service;

import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.model.Room;
import com.example.room_service.repository.RoomRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private  RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;


    @Nested
    class CreateRoom{


        @Test
        void shouldCreateRoom(){

            NewMemberDto newMemberDto = new NewMemberDto();
            newMemberDto.setUsername("walter");
            newMemberDto.setUserId("1247812");

            CreateRoomDto roomDto = new CreateRoomDto();
            roomDto.setTitle("Breaking Bad");

            Room room = new Room();
            room.setTitle(roomDto.getTitle());
            room.setId(UUID.randomUUID());

            when(roomRepository.save(any(Room.class))).thenReturn(room);

            Room created = roomService.createRoom(newMemberDto, roomDto);

            assertInstanceOf(UUID.class, created.getId());
            assertEquals(created.getTitle(), room.getTitle());
        }
    }

}
