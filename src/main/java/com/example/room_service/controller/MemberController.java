package com.example.room_service.controller;

import java.util.HashMap;
import java.util.UUID;

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
import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.model.Member;
import com.example.room_service.response_entity.ErrorResponse;
import com.example.room_service.service.MemberService;

import jakarta.validation.Valid;

@RequestMapping("members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/new/{roomId}")
    public ResponseEntity<?> join(@RequestBody @Valid NewMemberDto memberDto, @PathVariable("roomId") UUID roomId) {

        try {

            Member member = memberService.createMember(memberDto, roomId);

            HashMap<String, Object> data = new HashMap<>();

            data.put("id", member.getId());
            data.put("username", member.getUsername());
            data.put("userId", member.getUserId());
            data.put("roomId", member.getRoomId());
            data.put("joinedAt", member.getJoinedAt());

            return new ResponseEntity<>(data, HttpStatus.CREATED);

        } catch (RoomNotFoundException e) {
            return new ErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN).send();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") UUID memberId) {
        memberService.removeById(memberId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
