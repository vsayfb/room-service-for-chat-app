package com.example.room_service.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;
import java.util.UUID;

@Data
@RedisHash
public class Member {
    @Id
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    @Indexed
    private String userId;

    @Indexed
    private UUID roomId;

    @CreatedDate
    private Date joinedAt;
}
