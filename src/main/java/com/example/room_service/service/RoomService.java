package com.example.room_service.service;

import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.external.Client;
import com.example.room_service.model.Room;
import com.example.room_service.repository.RoomRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final MemberService memberService;

    public RoomService(RoomRepository roomRepository, MemberService memberService) {
        this.roomRepository = roomRepository;
        this.memberService = memberService;
    }

    Optional<Room> getById(ObjectId id){
        return roomRepository.findById(id);
    }

    Room createRoom(Client client,  CreateRoomDto roomDto){
        Room room = new Room();

        room.setTitle(roomDto.getTitle());

        Room savedRoom = roomRepository.save(room);

        memberService.createMember(client, savedRoom.getId());

        return savedRoom;
    }
}
