package com.example.room_service.controller;

import com.example.room_service.annotation.ClientSession;
import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.external.Client;
import com.example.room_service.model.Room;
import com.example.room_service.repository.projection.RoomWithoutMembersProjection;
import com.example.room_service.response_entity.ErrorResponse;
import com.example.room_service.response_entity.SuccessResponse;
import com.example.room_service.service.RoomService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequestMapping("/rooms")
@RestController
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public ResponseEntity<SuccessResponse<List<RoomWithoutMembersProjection>>> findAllRooms() {
        return new SuccessResponse<>(roomService.getAllRoomsWithoutMembers(), "All rooms found.", HttpStatus.OK).send();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> findRoomById(@PathVariable("roomId") ObjectId id) {

        Optional<Room> room = roomService.getRoomById(id);

        if (room.isEmpty()) {
            return new ErrorResponse("Room not found.", HttpStatus.NOT_FOUND).send();
        }

        return new SuccessResponse<>(room.get(), "Room found.", HttpStatus.OK).send();
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@ClientSession Client client, @RequestBody @Valid CreateRoomDto roomDto) {
        Room created = roomService.createRoom(client, roomDto);

        HashMap<String, Object> data = new HashMap<>();

        data.put("id", created.getId().toHexString());
        data.put("title", created.getTitle());
        data.put("createdAt", created.getCreatedAt());

        return new SuccessResponse<>(data, "Room is created successfully.", HttpStatus.CREATED).send();
    }

}
