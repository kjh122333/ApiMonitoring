package com.duzon.dbp.apimonitoring.service.socket;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.duzon.dbp.apimonitoring.dto.socket.SocketRoom;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ChatService
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SocketService {

    private final ObjectMapper objectMapper;
    private Map<String, SocketRoom> socketRooms;
 
    @PostConstruct
    private void init() {
        socketRooms = new LinkedHashMap<>();
    }
    
    public SocketRoom findRoomById(String roomId) {
        return socketRooms.get(roomId);
    }
    
    public SocketRoom createRoom(String id) {
        SocketRoom socketRoom = SocketRoom.builder()
            .roomId(id)
            .build();
        
        if (findRoomById(id) == null) {
            socketRooms.put(id, socketRoom);            
        }
        return socketRoom;
    }
    
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void remove(WebSocketSession session) {
        Iterator<SocketRoom> iter = socketRooms.values().iterator();
        while( iter.hasNext() ){
            SocketRoom sr = iter.next();
            sr.getSessions().remove(session);
            if (sr.getSessions().size() == 0) {
                iter.remove();
            }
        }
	}
}