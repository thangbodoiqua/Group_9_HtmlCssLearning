package com.se2.htmlcsslearning.ai;

import com.se2.htmlcsslearning.constant.ModelConstants;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class GroqModelConfig {

    @Value("${spring.ai.openai.api-key}")
    private String groqApiKey;

    @Value("${spring.ai.openai.base-url}")
    private String groqBaseUrl;

    @Bean
    public OpenAiApi groqApi() {
        return OpenAiApi.builder()
                .baseUrl(groqBaseUrl)
                .apiKey(groqApiKey)
                .build();
    }

    // ==================== Chat Models ====================

    @Bean
    @Qualifier("llama3_8b")
    public ChatModel llama38bChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.LLAMA_3_1_8B_INSTANT)
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("llama3_70b")
    public ChatModel llama370bChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.LLAMA_3_3_70B_VERSATILE)
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("gpt_oss_20b")
    public ChatModel gptOss20bChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.GPT_OSS_20B)
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("gpt_oss_120b")
    public ChatModel gptOss120bChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.GPT_OSS_120B)
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("llama4_scout")
    public ChatModel llama4ScoutChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.LLAMA_4_SCOUT)
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("prompt_guard_22m")
    public ChatModel promptGuard22mChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.PROMPT_GUARD_2_22M)
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("prompt_guard_86m")
    public ChatModel promptGuard86mChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.PROMPT_GUARD_2_86M)
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("gpt_oss_safeguard")
    public ChatModel gptOssSafeguardChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.GPT_OSS_SAFEGUARD_20B)
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("orpheus_arabic")
    public ChatModel orpheusArabicChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.ORPHEUS_ARABIC_SAUDI)
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("orpheus_english")
    public ChatModel orpheusEnglishChatModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.ORPHEUS_V1_ENGLISH)
                        .temperature(0.7)
                        .build())
                .build();
    }
    @Bean
    @Qualifier("groq_compound")
    public ChatModel groqCompoundModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.GROQ_COMPOUND)
                        .temperature(0.7)
                        .build())
                .build();
    }
    @Bean
    @Qualifier("groq_compound_mini")
    public ChatModel groqCompoundMiniModel(OpenAiApi groqApi) {
        return OpenAiChatModel.builder()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(ModelConstants.GROQ_COMPOUND_MINI)
                        .temperature(0.7)
                        .build())
                .build();
    }
    // ==================== Map ====================

    @Bean(name = "groqModelsMap")
    public Map<String, ChatModel> groqModels(
            @Qualifier("llama3_8b") ChatModel llama38b,
            @Qualifier("llama3_70b") ChatModel llama370b,
            @Qualifier("gpt_oss_20b") ChatModel gptOss20b,
            @Qualifier("gpt_oss_120b") ChatModel gptOss120b,
            @Qualifier("llama4_scout") ChatModel llama4Scout,
            @Qualifier("prompt_guard_22m") ChatModel promptGuard22m,
            @Qualifier("prompt_guard_86m") ChatModel promptGuard86m,
            @Qualifier("gpt_oss_safeguard") ChatModel gptOssSafeguard,
            @Qualifier("orpheus_arabic") ChatModel orpheusArabic,
            @Qualifier("groq_compound") ChatModel groqCompound,
            @Qualifier("groq_compound_mini") ChatModel groqCompoundMini
    ) {
        Map<String, ChatModel> map = new HashMap<>();
        map.put("llama3_8b", llama38b);
        map.put("llama3_70b", llama370b);
        map.put("gpt_oss_20b", gptOss20b);
        map.put("gpt_oss_120b", gptOss120b);
        map.put("llama4_scout", llama4Scout);
        map.put("prompt_guard_22m", promptGuard22m);
        map.put("prompt_guard_86m", promptGuard86m);
        map.put("gpt_oss_safeguard", gptOssSafeguard);
        map.put("groq_compound", gptOssSafeguard);
        map.put("groq_compound_mini", gptOssSafeguard);

        return map;
    }
}