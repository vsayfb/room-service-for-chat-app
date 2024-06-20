package com.example.room_service.controller;

import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.external.Client;
import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;
import com.example.room_service.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

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

            Client client = new Client();

            client.setUserId(String.valueOf(ObjectId.get()));
            client.setUsername("walter");

            CreateRoomDto roomDto = new CreateRoomDto();

            roomDto.setTitle("Why breaking bad is the best show ever?");

            Room room = new Room();

            room.setTitle(roomDto.getTitle());

            roomRepository.save(room);

            mockMvc.perform(get("/rooms/"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data[0].title", Is.is(roomDto.getTitle())))
                    .andExpect(jsonPath("$.data[0].id", any(String.class)))
                    .andExpect(jsonPath("$.data[0].createdAt", any(String.class)))
                    .andExpect(jsonPath("$.data[0].members").doesNotExist());

        }

    }

    @Nested
    class FindById {

        @Test
        void shouldReturn404() throws Exception {

            mockMvc.perform(get("/rooms/" + ObjectId.get()))
                    .andExpect(status().isNotFound());

        }

        @Test
        void shouldReturnRoom() throws Exception {

            Client client = new Client();

            client.setUserId(String.valueOf(ObjectId.get()));
            client.setUsername("walter");

            CreateRoomDto roomDto = new CreateRoomDto();

            roomDto.setTitle("Why breaking bad is the best show ever?");

            Room room = new Room();

            room.setTitle(roomDto.getTitle());

            Room saved = roomRepository.save(room);

            Member member = new Member();

            member.setRoomId(saved.getId());
            member.setUsername(client.getUsername());
            member.setUserId(client.getUserId());

            Member savedMember = memberRepository.save(member);

            mockMvc.perform(get("/rooms/" + saved.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.title", Is.is(roomDto.getTitle())))
                    .andExpect(jsonPath("$.data.id", any(String.class)))
                    .andExpect(jsonPath("$.data.createdAt", any(String.class)))
                    .andExpect(jsonPath("$.data.members").exists())
                    .andExpect(jsonPath("$.data.members[0].id", Is.is(savedMember.getId().toHexString())))
                    .andExpect(jsonPath("$.data.members[0].userId", Is.is(savedMember.getUserId())))
                    .andExpect(jsonPath("$.data.members[0].username", Is.is(savedMember.getUsername())))
                    .andExpect(jsonPath("$.data.members[0].roomId", Is.is(savedMember.getRoomId().toHexString())));

        }

    }

    @Nested
    class Create {

        @Test
        void shouldCreateRoom() throws Exception {

            Client client = new Client();

            client.setUserId(String.valueOf(ObjectId.get()));
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
                    .andExpect(jsonPath("$.data.id", Matchers.any(String.class)))
                    .andExpect(jsonPath("$.data.createdAt", Matchers.any(String.class)))
                    .andExpect(jsonPath("$.timestamp", Matchers.any(String.class)));

        }
    }

}
