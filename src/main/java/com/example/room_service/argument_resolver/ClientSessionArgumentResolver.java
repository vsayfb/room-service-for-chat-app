package com.example.room_service.argument_resolver;

import com.example.room_service.annotation.ClientSession;
import com.example.room_service.external.Client;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ClientSessionArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ClientSession.class) && parameter.getParameterType().equals(Client.class);
    }

    @Override
    public Client resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String username = webRequest.getHeader("x-jwt-username");
        String userId = webRequest.getHeader("x-jwt-userId");

        Client client = new Client();

        client.setUsername(username);
        client.setUserId(userId);

        return client;
    }
}
