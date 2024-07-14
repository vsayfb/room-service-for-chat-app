package com.example.room_service.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.model.Member;
import com.example.room_service.response_entity.ErrorResponse;
import com.example.room_service.service.MemberService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@RequestMapping("/members")
@RestController
@Hidden
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/room/{roomId}")
    public ResponseEntity<?> join(@RequestBody @Valid NewMemberDto memberDto, @PathVariable("roomId") UUID roomId) {

        try {

            Member member = memberService.createMember(memberDto, roomId);

            return new ResponseEntity<>(member, HttpStatus.CREATED);

        } catch (RoomNotFoundException e) {
            return new ErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN).send();
        }

    }

    @DeleteMapping("/{id}/session/{sessionId}")
    public ResponseEntity<?> remove(@PathVariable("id") UUID memberId, @PathVariable("sessionId") String sessionId) {
        memberService.removeById(memberId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/room/{roomId}/session/{sessionId}")
    public ResponseEntity<?> removeByUserId(
            @PathVariable("userId") String userId,
            @PathVariable("roomId") UUID roomId,
            @PathVariable("sessionId") String sessionId) {

        memberService.removeByUserIdAndRoomIdAndSessionId(userId, roomId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/room/{roomId}")
    public ResponseEntity<?> findMemberByUserIdAndRoomId(
            @PathVariable("userId") String userId,
            @PathVariable("roomId") UUID roomId) {

        Optional<Member> optionalMember = memberService.getByUserIdAndRoomId(userId, roomId);

        if (optionalMember.isPresent()) {
            return new ResponseEntity<>(optionalMember.get(), HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
