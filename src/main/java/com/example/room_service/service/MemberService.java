package com.example.room_service.service;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.model.Member;
import com.example.room_service.repository.MemberRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(NewMemberDto memberDto, ObjectId roomId) {

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
