/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.model.v2.event.Event;
import com.linecorp.bot.model.v2.event.MessageEvent;
import com.linecorp.bot.model.v2.event.message.MessageContent;
import com.linecorp.bot.model.v2.event.message.TextMessageContent;
import com.linecorp.bot.model.v2.message.TextMessage;
import com.linecorp.bot.model.v2.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class EchoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @RestController
    public static class MyController {
        @Autowired
        private LineBotClient lineBotClient;

        @RequestMapping("/callback")
        public void callback(@LineBotMessages List<Event> events) throws LineBotAPIException {
            for (Event event : events) {
                log.info("event: {}", event);
                if (event instanceof MessageEvent) {
                    MessageContent message = ((MessageEvent) event).getMessage();
                    if (message instanceof TextMessageContent) {
                        log.info("Sending reply message");
                        TextMessageContent textMessageContent = (TextMessageContent) message;
                        String mid = ((MessageEvent) event).getSource().getUserId();
                        BotApiResponse apiResponse = lineBotClient.push(Collections.singletonList(mid),
                                                                        Collections.singletonList(
                                                                                new TextMessage(
                                                                                        textMessageContent
                                                                                                .getText())));
                        log.info("Sent messages: {}", apiResponse);
                    }
                }
            }
        }
    }
}
