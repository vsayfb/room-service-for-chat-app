package com.example.room_service.service;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.MemberRepository;

import java.util.*;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final RoomService roomService;
    private final MemberRepository memberRepository;

    public MemberService(@Lazy RoomService roomService, MemberRepository memberRepository) {
        this.roomService = roomService;
        this.memberRepository = memberRepository;
    }

    public Set<Member> getFirstNMembersByRoomId(UUID roomId, int number) {
        return memberRepository.findByRoomId(roomId, Pageable.ofSize(number)).toSet();
    }

    public Member createMember(NewMemberDto memberDto, UUID roomId) throws RoomNotFoundException {

        Optional<Room> optionalRoom = roomService.getById(roomId);

        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException();
        }

        Optional<Member> optionalMember = memberRepository.findByUserIdAndRoomId(memberDto.getUserId(), roomId);

        if (optionalMember.isPresent()) {

            Member member = optionalMember.get();

            member.setJoinedAt(new Date());

            return memberRepository.save(member);
        }

        Member member = new Member();

        member.setUsername(memberDto.getUsername());
        member.setUserId(memberDto.getUserId());
        member.setRoomId(roomId);
        member.setJoinedAt(new Date());

        return memberRepository.save(member);
    }

    public void removeById(UUID memberId) {
        this.memberRepository.deleteById(memberId);
    }

    public Set<Member> getAllByRoomId(UUID id) {
        return memberRepository.findAllByRoomId(id);
    }
}
