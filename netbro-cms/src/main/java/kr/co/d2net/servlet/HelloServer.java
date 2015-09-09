package kr.co.d2net.servlet;

import javax.websocket.WebSocketEndpoint;
import javax.websocket.WebSocketMessage;

import kr.co.d2net.dto.xml.WebMessage;

@WebSocketEndpoint(
		value="/hello",
		encoders={WebMessage.class},
		decoders={WebMessage.class}
)
public class HelloServer {

	@WebSocketMessage
	public String sayHello(String name) {
        return "Hello " + name + "!";
    }
}
