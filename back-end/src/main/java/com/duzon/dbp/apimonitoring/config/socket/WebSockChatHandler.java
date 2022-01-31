package com.duzon.dbp.apimonitoring.config.socket;

import com.duzon.dbp.apimonitoring.advice.exception.ChannelNotFoundError;
import com.duzon.dbp.apimonitoring.dto.socket.SocketMessage;
import com.duzon.dbp.apimonitoring.dto.socket.SocketRoom;
import com.duzon.dbp.apimonitoring.service.NotificationService;
import com.duzon.dbp.apimonitoring.service.socket.SocketService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSockChatHandler
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSockChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final SocketService socketService;

    @Autowired
    NotificationService notificationService;

    // 메시지 전송 시
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);
        // 삭제 TextMessage textMessage = new TextMessage("Welcome chatting sever~^^ ");
        // 삭제 session.sendMessage(textMessage);
        SocketMessage socketMessage = objectMapper.readValue(payload, SocketMessage.class);
        if (socketMessage.getRoomId() == null) {
            CharSequence msg = "해당 채널이 없습니다.";
            session.sendMessage(new TextMessage(msg));
            session.close();
            throw new ChannelNotFoundError("해당 채널이 없습니다.");
        }
        if (socketService.findRoomById(socketMessage.getRoomId()) == null) {
            socketService.createRoom(socketMessage.getRoomId());
        }
        SocketRoom room = socketService.findRoomById(socketMessage.getRoomId());
        room.handleActions(session, socketMessage, socketService, notificationService);
    }

    // 소켓 연결 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("소켓이 연결 되었습니다.");
    }

    // 소켓 종료 시
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        socketService.remove(session);
        log.info("소켓이 종료 되었습니다.");
    }
}