package com.example.room_service.controller;

import com.example.room_service.annotation.ClientSession;
import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.dto.response.NewRoomDto;
import com.example.room_service.external.Client;
import com.example.room_service.model.Room;
import com.example.room_service.response_entity.ErrorResponse;
import com.example.room_service.response_entity.SuccessResponse;
import com.example.room_service.service.MemberService;
import com.example.room_service.service.RoomService;
import com.example.room_service.swagger.RoomApiResponse;
import com.example.room_service.swagger.RoomsApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/rooms")
@RestController
public class RoomController {

    private final RoomService roomService;
    private final MemberService memberService;

    public RoomController(RoomService roomService, MemberService memberService) {
        this.roomService = roomService;
        this.memberService = memberService;
    }

    @RoomsApiResponse
    @GetMapping("/")
    public ResponseEntity<SuccessResponse<Iterable<Room>>> findAllRooms() {

        Iterable<Room> rooms = roomService.getAllRooms();

        for (Room room : rooms) {
            room.setMembers(memberService.getFirstNMembersByRoomId(room.getId(), 3));
        }

        return new SuccessResponse<>(rooms, "All rooms found.", HttpStatus.OK).send();
    }

    @RoomApiResponse
    @GetMapping("/{id}")
    public ResponseEntity<?> findRoomById(@PathVariable("id") UUID id) {

        Optional<Room> room = roomService.getRoomById(id);

        if (room.isEmpty()) {
            return new ErrorResponse("Room not found.", HttpStatus.NOT_FOUND).send();
        }

        Room found = room.get();

        found.setMembers(memberService.getAllByRoomId(id));

        return new SuccessResponse<>(found, "Room found.", HttpStatus.OK).send();
    }

    @PostMapping("/")
    public ResponseEntity<SuccessResponse<NewRoomDto>> create(@ClientSession Client client,
            @RequestBody @Valid CreateRoomDto roomDto) {

        NewMemberDto memberDto = new NewMemberDto();

        memberDto.setUsername(client.getUsername());
        memberDto.setUserId(client.getUserId());

        Room created = roomService.createRoom(memberDto, roomDto);

        NewRoomDto newRoomDto = new NewRoomDto();

        newRoomDto.setId(created.getId());
        newRoomDto.setTitle(created.getTitle());
        newRoomDto.setCreatedAt(created.getCreatedAt());

        return new SuccessResponse<>(newRoomDto, "Room is created successfully.", HttpStatus.CREATED).send();
    }

}
