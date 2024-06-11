package com.example.room_service.model;

import com.example.room_service.validations.CreateRoomRules;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@Document
@Getter
@Setter
public class Room {

    @Id
    private ObjectId id;

    @NotNull
    @Length(min = CreateRoomRules.MIN_LENGTH, max = CreateRoomRules.MAX_LENGTH)
    private String title;

    @ReadOnlyProperty
    @DocumentReference(lookup="{'roomId':?#{#self._id} }" , lazy = true)
    private List<Member> members;

    @CreatedDate
    private Date createdAt;
}
