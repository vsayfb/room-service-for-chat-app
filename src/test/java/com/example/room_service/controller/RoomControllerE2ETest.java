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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;

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

            mockMvc.perform(get("/rooms"))
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

            mockMvc.perform(post("/rooms")
                            .content(mapper.writeValueAsString(roomDto)).contentType(MediaType.APPLICATION_JSON)
                            .header("x-jwt-username", client.getUsername())
                            .header("x-jwt-userId", client.getUserId())
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.title", Is.is(roomDto.getTitle())))
                    .andExpect(jsonPath("$.data.id", Matchers.any(String.class)))
                    .andExpect(jsonPath("$.data.createdAt", Matchers.any(String.class)))
                    .andExpect(jsonPath("$.timestamp", Matchers.any(String.class)));

        }
    }

    @Nested
    class Join {

        @Test
        void shouldThrowRoomNotFoundException() throws Exception {

            Client client = new Client();

            client.setUserId(String.valueOf(ObjectId.get()));
            client.setUsername("walter");

            ObjectId randomRoomId = ObjectId.get();

            mockMvc.perform(post("/rooms/join/" + randomRoomId)
                            .header("x-jwt-username", client.getUsername())
                            .header("x-jwt-userId", client.getUserId())
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message", Is.is("Room not found.")));
        }

        @Test
        void shouldThrowUserAlreadyInChatException() throws Exception {

            Client client = new Client();

            client.setUserId(String.valueOf(ObjectId.get()));
            client.setUsername("walter");

            Room room = new Room();

            room.setTitle("Why breaking bad is the best show ever?");

            Room savedRoom = roomRepository.save(room);

            Member member = new Member();

            member.setUserId(client.getUserId());
            member.setUsername(client.getUsername());
            member.setRoomId(savedRoom.getId());

            memberRepository.save(member);


            mockMvc.perform(post("/rooms/join/" + savedRoom.getId())
                            .header("x-jwt-username", client.getUsername())
                            .header("x-jwt-userId", client.getUserId())
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message", Is.is(String.format("User %s is already a member of this chat.", member.getUsername()))));
        }


        @Test
        void shouldJoinRoom() throws Exception {

            Client client = new Client();

            client.setUserId(String.valueOf(ObjectId.get()));
            client.setUsername("walter");

            Room room = new Room();

            room.setTitle("Why breaking bad is the best show ever?");

            Room savedRoom = roomRepository.save(room);

            mockMvc.perform(post("/rooms/join/" + savedRoom.getId())
                            .header("x-jwt-username", client.getUsername())
                            .header("x-jwt-userId", client.getUserId())
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.id", Matchers.any(String.class)))
                    .andExpect(jsonPath("$.data.username", Is.is(client.getUsername())))
                    .andExpect(jsonPath("$.data.userId", Is.is(client.getUserId())))
                    .andExpect(jsonPath("$.data.roomId", Is.is(savedRoom.getId().toHexString())))
                    .andExpect(jsonPath("$.data.joinedAt", Matchers.any(String.class)));
        }

    }

    @Nested
    class Leave {

        @Test
        void shouldThrowUserNotInChatException() throws Exception {

            Client client = new Client();

            client.setUserId(String.valueOf(ObjectId.get()));
            client.setUsername("walter");

            Room room = new Room();

            room.setTitle("Why breaking bad is the best show ever?");

            Room savedRoom = roomRepository.save(room);

            mockMvc.perform(post("/rooms/leave/" + savedRoom.getId())
                            .header("x-jwt-username", client.getUsername())
                            .header("x-jwt-userId", client.getUserId())
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message", Is.is(String.format("User %s does not belong to this chat.", client.getUsername()))));
        }

        @Test
        void shouldLeaveChat() throws Exception {

            Client client = new Client();

            client.setUserId(String.valueOf(ObjectId.get()));
            client.setUsername("walter");

            Room room = new Room();

            room.setTitle("Why breaking bad is the best show ever?");

            Room savedRoom = roomRepository.save(room);

            Member member = new Member();

            member.setUsername(client.getUsername());
            member.setUserId(client.getUserId());
            member.setRoomId(room.getId());

            memberRepository.save(member);

            mockMvc.perform(post("/rooms/leave/" + savedRoom.getId())
                            .header("x-jwt-username", client.getUsername())
                            .header("x-jwt-userId", client.getUserId())
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message", Is.is("Member is removed successfully.")));

            assertTrue(roomRepository.findById(savedRoom.getId()).isPresent());
            assertTrue(roomRepository.findById(savedRoom.getId()).get().getMembers().isEmpty());

        }

    }
}
