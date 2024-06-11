package com.example.room_service.service;

import com.example.room_service.exception.UserNotInChatException;
import com.example.room_service.external.Client;
import com.example.room_service.model.Member;
import com.example.room_service.repository.MemberRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Client client, ObjectId roomId) {
        Member member = new Member();

        member.setUsername(client.getUsername());
        member.setUserId(client.getUserId());

        member.setRoomId(roomId);

        return memberRepository.save(member);
    }

    public void deleteMemberInRoom(Client client, ObjectId roomId) throws UserNotInChatException {

        Optional<Member> optionalMember = memberRepository.findByUserIdAndRoomId(client.getUserId(), roomId);

        if(optionalMember.isEmpty()){
            throw  new UserNotInChatException(client.getUsername());
        }

        Member member = optionalMember.get();

        if(!(member.getUserId().equals(client.getUserId()))){
            throw  new UserNotInChatException(client.getUsername());
        }

        memberRepository.delete(member);
    }

}
