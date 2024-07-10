package com.example.room_service.contract;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.room_service.controller.MemberController;
import com.example.room_service.exception.RoomNotFoundException;
import com.example.room_service.model.Member;
import com.example.room_service.service.MemberService;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.spring.spring6.PactVerificationSpring6Provider;
import au.com.dius.pact.provider.spring.spring6.Spring6MockMvcTestTarget;

@Provider("RoomService")
@PactBroker(url = "${pactbroker.host}", authentication = @PactBrokerAuth(username = "${pactbroker.auth.username}", password = "${pactbroker.auth.password}"))
@IgnoreNoPactsToVerify
@WebMvcTest(MemberController.class)
public class MessageServiceContractVerificationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @TestTemplate
    @ExtendWith(PactVerificationSpring6Provider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {

        if (context != null) {
            context.verifyInteraction();
        }
    }

    @BeforeEach
    void before(PactVerificationContext context) {

        if (context != null) {
            context.setTarget(new Spring6MockMvcTestTarget(mockMvc));
        }

    }

    @State("a non-existent room")
    void nonExistentRoom() {

        when(memberService.createMember(any(), any())).thenThrow(RoomNotFoundException.class);
    }

    @State("an existent member")
    void existentMember() {

    }

    @State("an existent room")
    void existentRoom() {

        Member member = new Member();

        member.setId(UUID.randomUUID());
        member.setRoomId(UUID.randomUUID());
        member.setUsername("walter");
        member.setJoinedAt(new Date());
        member.addSessionId("1");
        member.setUserId(UUID.randomUUID().toString());

        when(memberService.createMember(any(), any())).thenReturn(member);

    }

    @State("an existent member by user id and room id")
    void existentMemberByUserIdAndRoomId() {

        Member member = new Member();

        member.setId(UUID.randomUUID());
        member.setRoomId(UUID.randomUUID());
        member.setUsername("walter");
        member.addSessionId("1");
        member.setJoinedAt(new Date());
        member.setUserId(UUID.randomUUID().toString());

        when(memberService.getByUserIdAndRoomId(any(), any())).thenReturn(Optional.of(member));
    }

    @State("an existent member by user id")
    void existentMemberByUserId() {

        Member member = new Member();

        member.setId(UUID.randomUUID());
        member.setRoomId(UUID.randomUUID());
        member.setUsername("walter");
        member.addSessionId("1");
        member.setJoinedAt(new Date());
        member.setUserId(UUID.randomUUID().toString());

        when(memberService.createMember(any(), any())).thenReturn(member);
    }

}
