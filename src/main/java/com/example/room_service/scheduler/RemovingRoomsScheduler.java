package com.example.room_service.scheduler;

import java.util.Set;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.room_service.model.Member;
import com.example.room_service.repository.MemberRepository;
import com.example.room_service.repository.RoomRepository;

@EnableScheduling
@Service
public class RemovingRoomsScheduler {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public RemovingRoomsScheduler(RoomRepository roomRepository, MemberRepository memberRepository) {
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
    }

    @Scheduled(fixedRate = 5000)
    void removeRoomsThatHaveNoMembers() {

        roomRepository.findAll().forEach((r) -> {

            Set<Member> members = memberRepository.findAllByRoomId(r.getId());

            if (members.size() == 0) {
                roomRepository.delete(r);
            }

        });

    }
}
