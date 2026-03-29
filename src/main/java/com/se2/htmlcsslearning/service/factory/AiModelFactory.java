package com.se2.htmlcsslearning.service.factory;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiModelFactory {

    @Autowired
    private ChatModel chatModel; 

    public ChatModel getModel(String modelId) {
      
        return chatModel;
    }
}
