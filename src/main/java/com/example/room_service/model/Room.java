package com.example.room_service.model;

import com.example.room_service.validations.CreateRoomRules;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

import java.util.*;


@Data
@RedisHash
public class Room {

    @Id
    private UUID id;

    @NotNull
    @Length(min = CreateRoomRules.MIN_LENGTH, max = CreateRoomRules.MAX_LENGTH)
    private String title;

    @Reference
    private Set<Member> members = new HashSet<>();

    @CreatedDate
    private Date createdAt;
}
