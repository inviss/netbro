package tutorial01.transfer;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

@Component
public class TransferSocketHandler extends BinaryWebSocketHandler {
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("After Connection Established !!!!");
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        session.sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("After Connection Closed !!!!");
    }
	
}
