package com.example.room_service.repository.projection;

import java.util.Date;

public interface RoomWithoutMembersProjection {

    String getId();
    String getTitle();
    Date getCreatedAt();
}
