package ch.uzh.ifi.hase.soprafs23.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import ch.uzh.ifi.hase.soprafs23.websocket.SocketHandler;
import ch.uzh.ifi.hase.soprafs23.websocket.PlayerHandler;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler(), "/socket")
                .setAllowedOrigins("*");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/game")
                // .setHandshakeHandler(new DefaultHandshakeHandler() {
                //     //Get sessionId from request and set it in Map attributes
                //     public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map attributes) throws Exception {
                //         if (request instanceof ServletServerHttpRequest) {
                //             ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                //             HttpSession session = servletRequest
                //                     .getServletRequest().getSession();
                //             attributes.put("sessionId", session.getId());
                //         }
                //         return true;
                //     }
                // })
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue/", "/topic/");
        registry.setUserDestinationPrefix("/user/");
        // registry.setApplicationDestinationPrefixes("/app");
    }

}
