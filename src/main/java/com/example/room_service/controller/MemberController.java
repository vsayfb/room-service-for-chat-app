package com.example.room_service.controller;

import java.util.HashMap;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.model.Member;
import com.example.room_service.service.MemberService;

@RequestMapping("members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/new/{roomId}")
    public ResponseEntity<?> join(@RequestBody NewMemberDto memberDto, @PathVariable("roomId") String roomId) {
        Member member = memberService.createMember(memberDto, new ObjectId(roomId));

        HashMap<String, Object> data = new HashMap<>();

        data.put("id", member.getId().toHexString());
        data.put("username", member.getUsername());
        data.put("userId", member.getUserId());
        data.put("roomId", member.getRoomId().toHexString());
        data.put("joinedAt", member.getJoinedAt());

        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> remove(@PathVariable("memberId") String memberId) {
        memberService.removeById(new ObjectId(memberId));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
