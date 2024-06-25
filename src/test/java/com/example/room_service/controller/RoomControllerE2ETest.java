package com.example.room_service.controller;

import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.external.Client;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;
import com.example.room_service.repository.RoomRepository;
import com.example.room_service.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

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
    class FindAll {

        @Test
        void shouldReturnAllRooms() throws Exception {

            NewMemberDto newMemberDto = new NewMemberDto();

            newMemberDto.setUserId("123456");
            newMemberDto.setUsername("walter");

            CreateRoomDto roomDto = new CreateRoomDto();

            roomDto.setTitle("Why breaking bad is the best show ever?");

            roomService.createRoom(newMemberDto, roomDto);

            mockMvc.perform(get("/rooms/"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data[0].title", Is.is(roomDto.getTitle())))
                    .andExpect(jsonPath("$.data[0].id", any(String.class)))
                    .andExpect(jsonPath("$.data[0].createdAt", any(String.class)))
                    .andExpect(jsonPath("$.data[0].members[0].username", Is.is(newMemberDto.getUsername())))
                    .andExpect(jsonPath("$.data[0].members[0].userId", Is.is(newMemberDto.getUserId())));
        }

    }

    @Nested
    class FindById {

        @Test
        void shouldReturn404() throws Exception {

            mockMvc.perform(get("/rooms/" + UUID.randomUUID()))
                    .andExpect(status().isNotFound());

        }

        @Test
        void shouldReturnRoom() throws Exception {

            NewMemberDto newMemberDto = new NewMemberDto();

            newMemberDto.setUserId("123456");
            newMemberDto.setUsername("walter");

            CreateRoomDto roomDto = new CreateRoomDto();

            roomDto.setTitle("Why breaking bad is the best show ever?");

            Room savedRoom = roomService.createRoom(newMemberDto, roomDto);

            mockMvc.perform(get("/rooms/" + savedRoom.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.title", Is.is(roomDto.getTitle())))
                    .andExpect(jsonPath("$.data.id", any(String.class)))
                    .andExpect(jsonPath("$.data.createdAt", any(String.class)))
                    .andExpect(jsonPath("$.data.members").exists())
                    .andExpect(jsonPath("$.data.members[0].userId", Is.is(newMemberDto.getUserId())))
                    .andExpect(jsonPath("$.data.members[0].username", Is.is(newMemberDto.getUsername())))
                    .andExpect(jsonPath("$.data.members[0].roomId", Is.is(String.valueOf(savedRoom.getId()))));

        }

    }

    @Nested
    class Create {

        @Test
        void shouldCreateRoom() throws Exception {

            Client client = new Client();

            client.setUserId(UUID.randomUUID().toString());
            client.setUsername("walter");

            CreateRoomDto roomDto = new CreateRoomDto();

            roomDto.setTitle("Why breaking bad is the best show ever?");

            ObjectMapper mapper = new ObjectMapper();

            mockMvc.perform(post("/rooms/")
                            .content(mapper.writeValueAsString(roomDto)).contentType(MediaType.APPLICATION_JSON)
                            .header("x-jwt-username", client.getUsername())
                            .header("x-jwt-userId", client.getUserId()))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.title", Is.is(roomDto.getTitle())))
                    .andExpect(jsonPath("$.data.id", any(String.class)))
                    .andExpect(jsonPath("$.data.createdAt", any(String.class)))
                    .andExpect(jsonPath("$.timestamp", Matchers.any(String.class)));

        }
    }

}
