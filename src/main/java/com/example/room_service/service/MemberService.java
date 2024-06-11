package com.example.room_service.service;

import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.exception.UserAlreadyInChatException;
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

    private final RoomService roomService;

    public MemberService(MemberRepository memberRepository, RoomService roomService) {
        this.memberRepository = memberRepository;
        this.roomService = roomService;
    }

    public Member createMember(Client client, ObjectId roomId) throws RoomNotFoundException, UserAlreadyInChatException {

        if(roomService.getById(roomId).isEmpty()){
            throw new RoomNotFoundException();
        }

        Optional<Member> optionalMember = memberRepository.findByUserIdAndRoomId(client.getUserId(), roomId);

        if(optionalMember.isPresent()){
            throw new UserAlreadyInChatException(client.getUsername());
        }

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
