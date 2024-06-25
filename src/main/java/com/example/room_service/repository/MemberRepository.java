package com.example.room_service.repository;

import com.example.room_service.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends CrudRepository<Member, UUID>, PagingAndSortingRepository<Member, UUID> {

    Page<Member> findByRoomId(UUID roomId, Pageable pageable);

    Optional<Member> findByRoomId(UUID roomId);

    Optional<Member> findByUserId(String userId);
}
