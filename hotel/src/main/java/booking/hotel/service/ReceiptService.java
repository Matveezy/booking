package booking.hotel.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ReceiptService implements StompSessionHandler {

    @Value("${websocket.url}")
    private String wsUrl;

    private StompSession stompSession;
    @EventListener(value = ApplicationReadyEvent.class)
    public void connect() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            stompSession = stompClient
                    .connectAsync(wsUrl + "/ws", this)
                    .get();
        } catch (Exception ignore) {}
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.err.println("Connection to STOMP server established.\n" +
                "Session: " + session + "\n" +
                "Headers: " + connectedHeaders + "\n");

        stompSession.subscribe("/topic/ws", this);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        String base64 = (String) payload;
        byte[] decoded = Base64.getDecoder().decode(base64);
        System.err.println("Received message:" + new String(decoded));
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        if (!session.isConnected()) {
            connect();
        }
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    public void sendMessage(File file) {
        stompSession.send("/app/ws", file);
    }

    @PreDestroy
    void onShutDown() {
        if (stompSession != null) {
            stompSession.disconnect();
        }
    }
}
