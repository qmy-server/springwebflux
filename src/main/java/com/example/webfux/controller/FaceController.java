package com.example.webfux.controller;

import com.example.webfux.config.WebSocketConfig.WebSocketMapping;
import com.example.webfux.service.FaceControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
@WebSocketMapping("/echo")
public class FaceController implements WebSocketHandler, CorsConfigurationSource {
    @Autowired
    private FaceControlService faceControlService;

    @Override
    public Mono<Void> handle(final WebSocketSession session) {
        //获取前端发过来的消息
      /*  return session.send(
                session.receive()
                        .map(msg -> session.textMessage(userService.findAll())
                        )
        );*/
        final String sessionId = session.getId();
        Flux<WebSocketMessage> output = session.receive()
               .concatMap(mapper -> faceControlService.findAllFace(mapper.getPayloadAsText())).map(value -> {
                    return session.textMessage(value.toString());
                });


        return session.send(output)
                .doFinally(e -> {
                    session.close();
                   /* try {
                        Thread.sleep(5000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }*/
                    log.error("doFinally");
                    System.out.println("doFinally,通道断开，session关闭");
                });
    }


    @Override
    public CorsConfiguration getCorsConfiguration(ServerWebExchange serverWebExchange) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        return configuration;
    }
}