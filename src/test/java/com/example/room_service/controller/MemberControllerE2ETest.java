package com.example.room_service.controller;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;
import com.example.room_service.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerE2ETest {

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
    class NewMember {

        @Test
        void shouldReturn403IfRoomNotExists() throws Exception {

            NewMemberDto newMemberDto = new NewMemberDto();

            newMemberDto.setUserId(UUID.randomUUID().toString());
            newMemberDto.setUsername("walter");

            ObjectMapper objectMapper = new ObjectMapper();

            String content = objectMapper.writeValueAsString(newMemberDto);

            UUID randomRoomId = UUID.randomUUID();

            mockMvc.perform(post("/members/new/" + randomRoomId)
                    .content(content)
                    .header("content-type", MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isForbidden())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message", Is.is("Room not found.")));
        }

        @Test
        void shouldReturn201() throws Exception {

            Room room = new Room();

            room.setTitle("An arbitrary title");

            Room newRoom = roomRepository.save(room);

            NewMemberDto newMemberDto = new NewMemberDto();

            newMemberDto.setUserId(UUID.randomUUID().toString());
            newMemberDto.setUsername("walter");

            ObjectMapper objectMapper = new ObjectMapper();

            String content = objectMapper.writeValueAsString(newMemberDto);

            mockMvc.perform(post("/members/new/" + newRoom.getId())
                    .content(content)
                    .header("content-type", MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

    }

    @Nested
    class DeleteMember {

        @Test
        void shouldReturn200() throws Exception {

            Member member = new Member();

            member.setUserId(UUID.randomUUID().toString());
            member.setUsername("walter");

            Member newMember = memberRepository.save(member);

            mockMvc.perform(delete("/members/" + newMember.getId()))
                    .andExpect(status().isOk());
        }

    }

}
