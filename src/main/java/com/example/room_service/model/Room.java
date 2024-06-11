package com.example.room_service.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@Document
@Data
public class Room {

    @Id
    private ObjectId id;

    @NotNull
    private String title;

    @ReadOnlyProperty
    @DocumentReference(lookup="{'roomId':?#{#self._id} }" , lazy = true)
    private List<Member> members;

    @CreatedDate
    private Date createdAt;
}
