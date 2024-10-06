package com.team5.pyeonjip.chat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    // 새로운 WebSocket 연결이 열렸을 때 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        sessions.put(session.getId(), session);
        System.out.println("새로운 세션 연결 : " + session.getId());
    }

    // 클라이언트로부터 메시지가 수신되면 호출, 모든 연결된 세션에게 브로드캐스트
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        //메시지 수신
        String payload = message.getPayload();
        System.out.println("수신된 메시지 " + payload);

        //메시지 브로드캐스트
        for(WebSocketSession webSocketSession : sessions.values()){
            webSocketSession.sendMessage(new TextMessage(payload));
        }
    }

    // WebSocket 연결이 닫히면 호출. 해당 세션 제거
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        sessions.remove(session.getId());
        System.out.println("세션 종료 : " + session.getId());
    }
}
