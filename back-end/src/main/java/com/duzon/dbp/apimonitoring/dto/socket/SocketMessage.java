package com.duzon.dbp.apimonitoring.dto.socket;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * ChatMessage
 */
@Getter
@Setter
public class SocketMessage {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        JOIN, STAY
    }

    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private Map<String, Object> message; // 메시지
}