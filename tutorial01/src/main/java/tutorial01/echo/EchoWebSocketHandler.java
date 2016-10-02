package tutorial01.echo;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class EchoWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payloadMessage = (String) message.getPayload();
        session.sendMessage(new TextMessage("ECHO : " + payloadMessage));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Connection이 구성된 후, 호출되는 method
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Connection이 종료된 후, 호출되는 method
        super.afterConnectionClosed(session, status);
    }
}
