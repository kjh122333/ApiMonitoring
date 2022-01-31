package com.duzon.dbp.apimonitoring.dto.socket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.duzon.dbp.apimonitoring.service.NotificationService;
import com.duzon.dbp.apimonitoring.service.socket.SocketService;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Getter;

/**
 * ChatRoom
 */
@Getter
public class SocketRoom {
    private String roomId;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public SocketRoom(String roomId) {
        this.roomId = roomId;
    }

    public void handleActions(WebSocketSession session, SocketMessage chatMessage, SocketService chatService, NotificationService notificationService) {
        if (chatMessage.getType().equals(SocketMessage.MessageType.JOIN)) {
            sessions.add(session);

            Map<String, Object> map = new HashMap<>();
            map.put("count", notificationService.NotificationListCount(chatMessage.getRoomId()));
            
            chatMessage.setMessage(map);

            sendMessage(chatMessage, chatService);
        }
    }

    public <T> void sendMessage(T message, SocketService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }
}