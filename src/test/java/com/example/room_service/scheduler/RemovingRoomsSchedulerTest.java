package com.example.room_service.scheduler;

import java.util.concurrent.Callable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;
import com.example.room_service.repository.RoomRepository;

import static org.awaitility.Awaitility.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class RemovingRoomsSchedulerTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Callable<Boolean> emptyRoomsAreRemoved() {
        return () -> roomRepository.count() == 1;
    }

    @BeforeEach
    @AfterEach
    void cleanUp() {
        roomRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test()
    void test() throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            Room room = new Room();
            room.setTitle("First room - " + i);

            roomRepository.save(room);
        }

        Room roomWithMembers = new Room();

        roomWithMembers.setTitle("Walter White's Room");

        roomRepository.save(roomWithMembers);

        assertEquals(roomRepository.count(), 11);

        Member member = new Member();

        member.setUsername("walter");
        member.setRoomId(roomWithMembers.getId());

        memberRepository.save(member);

        await().until(emptyRoomsAreRemoved());

        assertEquals(roomRepository.count(), 1);
        assertTrue(roomRepository.findById(roomWithMembers.getId()).isPresent());

    }
}
