package ai.chat2db.server.web.api.controller.ai.deepseek.client;

import ai.chat2db.server.domain.api.model.Config;
import ai.chat2db.server.domain.api.service.ConfigService;
import ai.chat2db.server.web.api.controller.ai.fastchat.client.FastChatAIStreamClient;
import ai.chat2db.server.web.api.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * DeepSeek AI Client — OpenAI-compatible API.
 * Default host: https://api.deepseek.com/v1/chat/completions
 *
 * @author moji
 */
@Slf4j
public class DeepSeekAIClient {

    public static final String DEEPSEEK_API_KEY = "deepseek.apiKey";

    public static final String DEEPSEEK_HOST = "deepseek.apiHost";

    public static final String DEEPSEEK_MODEL = "deepseek.model";

    private static FastChatAIStreamClient DEEPSEEK_AI_CLIENT;

    public static FastChatAIStreamClient getInstance() {
        if (DEEPSEEK_AI_CLIENT != null) {
            return DEEPSEEK_AI_CLIENT;
        }
        return singleton();
    }

    private static FastChatAIStreamClient singleton() {
        if (DEEPSEEK_AI_CLIENT == null) {
            synchronized (DeepSeekAIClient.class) {
                if (DEEPSEEK_AI_CLIENT == null) {
                    refresh();
                }
            }
        }
        return DEEPSEEK_AI_CLIENT;
    }

    public static void refresh() {
        String apiKey = "";
        String apiHost = "";
        String model = "";
        ConfigService configService = ApplicationContextUtil.getBean(ConfigService.class);
        Config apiHostConfig = configService.find(DEEPSEEK_HOST).getData();
        if (apiHostConfig != null && StringUtils.isNotBlank(apiHostConfig.getContent())) {
            apiHost = apiHostConfig.getContent();
        }
        Config config = configService.find(DEEPSEEK_API_KEY).getData();
        if (config != null && StringUtils.isNotBlank(config.getContent())) {
            apiKey = config.getContent();
        }
        Config deployConfig = configService.find(DEEPSEEK_MODEL).getData();
        if (deployConfig != null && StringUtils.isNotBlank(deployConfig.getContent())) {
            model = deployConfig.getContent();
        }
        DEEPSEEK_AI_CLIENT = FastChatAIStreamClient.builder()
            .apiKey(apiKey)
            .apiHost(apiHost)
            .model(model)
            .build();
    }
}
