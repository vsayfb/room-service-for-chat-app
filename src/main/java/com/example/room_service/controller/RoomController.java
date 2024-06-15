package com.example.room_service.controller;

import com.example.room_service.annotation.ClientSession;
import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.exception.UserAlreadyInChatException;
import com.example.room_service.exception.UserNotInChatException;
import com.example.room_service.external.Client;
import com.example.room_service.model.Member;
import com.example.room_service.model.Room;
import com.example.room_service.repository.projection.RoomWithoutMembersProjection;
import com.example.room_service.response_entity.ErrorResponse;
import com.example.room_service.response_entity.SuccessResponse;
import com.example.room_service.service.MemberService;
import com.example.room_service.service.RoomService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequestMapping("rooms")
@RestController
public class RoomController {

    private final RoomService roomService;
    private final MemberService memberService;

    public RoomController(RoomService roomService, MemberService memberService) {
        this.roomService = roomService;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<RoomWithoutMembersProjection>>> findAllRooms() {
        return new SuccessResponse<>(roomService.getAllRoomsWithoutMembers(), "All rooms found.", HttpStatus.OK).send();
    }

    @GetMapping("{roomId}")
    public ResponseEntity<?> findRoomById(@PathVariable("roomId") ObjectId id) {

        Optional<Room> room = roomService.getRoomById(id);

        if (room.isEmpty()) {
            return new ErrorResponse("Room not found.", HttpStatus.NOT_FOUND).send();
        }

        return new SuccessResponse<>(room.get(), "Room found.", HttpStatus.OK).send();
    }


    @PostMapping
    public ResponseEntity<?> create(@ClientSession Client client, @RequestBody @Valid CreateRoomDto roomDto) {
        Room created = roomService.createRoom(client, roomDto);

        HashMap<String, Object> data = new HashMap<>();

        data.put("id", created.getId().toHexString());
        data.put("title", created.getTitle());
        data.put("createdAt", created.getCreatedAt());

        return new SuccessResponse<>(data, "Room is created successfully.", HttpStatus.CREATED).send();
    }

    @PostMapping("join/{roomId}")
    public ResponseEntity<?> join(@ClientSession Client client, @PathVariable("roomId") String roomId) {
        try {
            Member member = memberService.createMember(client, new ObjectId(roomId));

            HashMap<String, Object> data = new HashMap<>();

            data.put("id", member.getId().toHexString());
            data.put("username", member.getUsername());
            data.put("userId", member.getUserId());
            data.put("roomId", member.getRoomId().toHexString());
            data.put("joinedAt", member.getJoinedAt());

            return new SuccessResponse<>(data, "Member is created successfully.", HttpStatus.CREATED).send();
        } catch (RoomNotFoundException | UserAlreadyInChatException e) {
            return new ErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN).send();
        }
    }

    @PostMapping("leave/{roomId}")
    public ResponseEntity<?> leave(@ClientSession Client client, @PathVariable("roomId") String roomId) {
        try {
            memberService.deleteMemberInRoom(client, new ObjectId(roomId));

            return new SuccessResponse<>(null, "Member is removed successfully.", HttpStatus.OK).send();
        } catch (UserNotInChatException e) {
            return new ErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN).send();
        }
    }
}
