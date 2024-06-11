package com.example.room_service.repository;

import com.example.room_service.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class MemberRepositoryIntTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach(){
        memberRepository.deleteAll();
    }

    @Test
    void testFindByUsername(){

        Member member = new Member();

        member.setUsername("gus");

        Member savedMember = memberRepository.save(member);

        assertEquals(memberRepository.findByUsername("gus").get().getUsername(), "gus");

        memberRepository.delete(savedMember);

        assertTrue(memberRepository.findByUsername("gus").isEmpty());
    }
}
