package com.se2.htmlcsslearning.service.factory;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatModelFactory {

    private final Map<String, ChatModel> models;

    @Autowired
    public ChatModelFactory(@Qualifier("groqModelsMap") Map<String, ChatModel> models
    , @Qualifier("openAiChatModel") ChatModel defaultChatModel) {
        this.models = models;
    }

    public ChatModel getModel(String modelName) {
        ChatModel model = models.get(modelName);
        if (model == null) {
            throw new IllegalArgumentException("Cannot find model: " + modelName);
        }
        return model;
    }
}

