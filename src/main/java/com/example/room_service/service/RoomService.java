package com.example.room_service.service;

import com.example.room_service.dto.request.CreateRoomDto;
import com.example.room_service.dto.response.NewMemberDto;
import com.example.room_service.model.Room;
import com.example.room_service.repository.RoomRepository;
import com.example.room_service.repository.projection.RoomWithoutMembersProjection;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final MemberService memberService;

    public RoomService(RoomRepository roomRepository, @Lazy MemberService memberService) {
        this.roomRepository = roomRepository;
        this.memberService = memberService;
    }

    public List<RoomWithoutMembersProjection> getAllRoomsWithoutMembers() {
        return roomRepository.findAllProjectBy();
    }

    public Optional<Room> getRoomById(UUID id) {
        return roomRepository.findById(id);
    }

    public Optional<Room> getById(UUID id) {
        return roomRepository.findById(id);
    }

    public Room createRoom(NewMemberDto newMemberDto, CreateRoomDto roomDto) {
        Room room = new Room();

        room.setTitle(roomDto.getTitle());

        Room savedRoom = roomRepository.save(room);

        memberService.createMember(newMemberDto, savedRoom.getId());

        return savedRoom;
    }
}
