package com.example.room_service.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Member {
    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private ObjectId id;

    @NotNull
    private String username;

    @NotNull
    private String userId;

    @JsonSerialize(using= ToStringSerializer.class)
    private ObjectId roomId;

    @CreatedDate
    private Date joinedAt;
}
