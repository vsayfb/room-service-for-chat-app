package com.example.room_service.service;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final RoomService roomService;
    private final MemberRepository memberRepository;

    public MemberService(RoomService roomService, MemberRepository memberRepository) {
        this.roomService = roomService;
        this.memberRepository = memberRepository;
    }

    public Member createMember(NewMemberDto memberDto, ObjectId roomId) throws RoomNotFoundException {

        Optional<Room> optionalRoom = roomService.getById(roomId);

        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException();
        }

        Member member = new Member();

        member.setUsername(memberDto.getUsername());
        member.setUserId(memberDto.getUserId());

        member.setRoomId(roomId);

        return memberRepository.save(member);
    }

    public void removeById(ObjectId memberId) {
        this.memberRepository.deleteById(memberId);
    }

}
