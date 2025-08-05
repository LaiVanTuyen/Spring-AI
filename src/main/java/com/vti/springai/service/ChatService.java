package com.vti.springai.service;

import com.vti.springai.dto.BillItem;
import com.vti.springai.dto.ChatRequest;
import com.vti.springai.dto.ExpenseInfo;
import com.vti.springai.dto.FilmInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    public ExpenseInfo chat(ChatRequest request) {
        SystemMessage systemMessage = new SystemMessage("""
                You are VTI.AI
                You should response with a formal voice
                """);

        UserMessage userMessage = new UserMessage(request.message());

        Prompt prompt = new Prompt(systemMessage, userMessage);

        return chatClient
                .prompt(prompt)
                .call()
                .entity(ExpenseInfo.class);
    }

    public List<BillItem> chatWithImage(MultipartFile file, String message) {
        Media media = Media.builder()
                .mimeType(MimeTypeUtils.parseMimeType(Objects.requireNonNull(file.getContentType())))
                .data(file.getResource())
                .build();

        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(1D)
                .build();

        return chatClient.prompt()
                .options(chatOptions)
                .system("You are VTI.AI.")
                .user(promptUserSpec
                    -> promptUserSpec.media(media)
                    .text(message))
                .call()
                .entity(new ParameterizedTypeReference<List<BillItem>>() {
                });
    }
}
