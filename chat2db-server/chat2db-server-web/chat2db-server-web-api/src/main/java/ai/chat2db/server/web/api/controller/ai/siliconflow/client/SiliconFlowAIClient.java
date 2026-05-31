package ai.chat2db.server.web.api.controller.ai.siliconflow.client;

import ai.chat2db.server.domain.api.model.Config;
import ai.chat2db.server.domain.api.service.ConfigService;
import ai.chat2db.server.web.api.controller.ai.fastchat.client.FastChatAIStreamClient;
import ai.chat2db.server.web.api.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * SiliconFlow AI Client — OpenAI-compatible API.
 * Default host: https://api.siliconflow.cn/v1/chat/completions
 *
 * @author moji
 */
@Slf4j
public class SiliconFlowAIClient {

    public static final String SILICONFLOW_API_KEY = "siliconflow.apiKey";

    public static final String SILICONFLOW_HOST = "siliconflow.apiHost";

    public static final String SILICONFLOW_MODEL = "siliconflow.model";

    private static FastChatAIStreamClient SILICONFLOW_AI_CLIENT;

    public static FastChatAIStreamClient getInstance() {
        if (SILICONFLOW_AI_CLIENT != null) {
            return SILICONFLOW_AI_CLIENT;
        }
        return singleton();
    }

    private static FastChatAIStreamClient singleton() {
        if (SILICONFLOW_AI_CLIENT == null) {
            synchronized (SiliconFlowAIClient.class) {
                if (SILICONFLOW_AI_CLIENT == null) {
                    refresh();
                }
            }
        }
        return SILICONFLOW_AI_CLIENT;
    }

    public static void refresh() {
        String apiKey = "";
        String apiHost = "";
        String model = "";
        ConfigService configService = ApplicationContextUtil.getBean(ConfigService.class);
        Config apiHostConfig = configService.find(SILICONFLOW_HOST).getData();
        if (apiHostConfig != null && StringUtils.isNotBlank(apiHostConfig.getContent())) {
            apiHost = apiHostConfig.getContent();
        }
        Config config = configService.find(SILICONFLOW_API_KEY).getData();
        if (config != null && StringUtils.isNotBlank(config.getContent())) {
            apiKey = config.getContent();
        }
        Config deployConfig = configService.find(SILICONFLOW_MODEL).getData();
        if (deployConfig != null && StringUtils.isNotBlank(deployConfig.getContent())) {
            model = deployConfig.getContent();
        }
        SILICONFLOW_AI_CLIENT = FastChatAIStreamClient.builder()
            .apiKey(apiKey)
            .apiHost(apiHost)
            .model(model)
            .build();
    }
}
