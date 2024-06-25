package com.example.room_service.service;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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

    public Member createMember(NewMemberDto memberDto, UUID roomId) throws RoomNotFoundException {

        Optional<Room> optionalRoom = roomService.getById(roomId);

        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException();
        }

        Optional<Member> optionalMember = memberRepository.findByUserId(memberDto.getUserId());

        if (optionalMember.isPresent()) {

            Member member = optionalMember.get();

            member.setJoinedAt(new Date());

            return memberRepository.save(member);
        }

        Member member = new Member();

        member.setUsername(memberDto.getUsername());
        member.setUserId(memberDto.getUserId());
        member.setRoomId(roomId);

        return memberRepository.save(member);
    }

    public void removeById(UUID memberId) {
        this.memberRepository.deleteById(memberId);
    }

}
