package top.daoha.trigger.llm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@Component
public class DeepSeekClient {

    @Resource
    private DeepSeekProperties properties;

    public String chat(String systemPrompt, String userPrompt) {
        if (StringUtils.isBlank(properties.getApiKey())) {
            return "AI 服务还没有配置 API Key。请在服务环境变量中设置 DEEPSEEK_API_KEY 后再使用。";
        }

        int attempts = Math.max(1, properties.getMaxRetries() == null ? 1 : properties.getMaxRetries());
        RuntimeException lastException = null;

        for (int i = 1; i <= attempts; i++) {
            try {
                return doChat(systemPrompt, userPrompt);
            } catch (RuntimeException e) {
                lastException = e;
                log.warn("DeepSeek 调用失败，第 {}/{} 次", i, attempts, e);
                sleepBeforeRetry(i, attempts);
            }
        }

        throw lastException == null ? new RuntimeException("DeepSeek 调用失败") : lastException;
    }

    private String doChat(String systemPrompt, String userPrompt) {
        String url = normalizeBaseUrl(properties.getBaseUrl()) + "/chat/completions";

        JSONObject request = new JSONObject();
        request.put("model", properties.getModel());
        request.put("temperature", 0.6);
        request.put("max_tokens", 900);

        JSONArray messages = new JSONArray();
        messages.add(message("system", systemPrompt));
        messages.add(message("user", userPrompt));
        request.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());

        ResponseEntity<String> response = createRestTemplate().postForEntity(
                url,
                new HttpEntity<>(request.toJSONString(), headers),
                String.class);

        JSONObject body = JSON.parseObject(response.getBody());
        if (body == null) {
            throw new RuntimeException("DeepSeek 响应为空");
        }

        JSONObject error = body.getJSONObject("error");
        if (error != null) {
            String message = StringUtils.defaultIfBlank(error.getString("message"), error.toJSONString());
            throw new RuntimeException("DeepSeek 错误: " + message);
        }

        JSONArray choices = body.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("DeepSeek 响应为空");
        }

        JSONObject choice = choices.getJSONObject(0);
        String content = extractContent(choice);
        if (StringUtils.isBlank(content)) {
            log.warn("DeepSeek 未返回可展示内容，finishReason={}, response={}",
                    choice == null ? null : choice.getString("finish_reason"),
                    StringUtils.abbreviate(response.getBody(), 1200));
            return "AI 这次没有返回完整内容，请稍后再点一次试试。";
        }
        return content.trim();
    }

    private String extractContent(JSONObject choice) {
        if (choice == null) {
            return null;
        }

        JSONObject message = choice.getJSONObject("message");
        String content = firstNotBlank(
                message == null ? null : message.getString("content"),
                message == null ? null : message.getString("reasoning_content"),
                choice.getString("text"));
        if (StringUtils.isNotBlank(content)) {
            return content;
        }

        JSONObject delta = choice.getJSONObject("delta");
        return firstNotBlank(
                delta == null ? null : delta.getString("content"),
                delta == null ? null : delta.getString("reasoning_content"));
    }

    private String firstNotBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private RestTemplate createRestTemplate() {
        int timeoutMillis = Math.max(5, properties.getTimeout() == null ? 120 : properties.getTimeout()) * 1000;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeoutMillis);
        factory.setReadTimeout(timeoutMillis);
        return new RestTemplate(factory);
    }

    private JSONObject message(String role, String content) {
        JSONObject message = new JSONObject();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    private String normalizeBaseUrl(String baseUrl) {
        String value = StringUtils.defaultIfBlank(baseUrl, "https://api.deepseek.com").trim();
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private void sleepBeforeRetry(int attempt, int attempts) {
        if (attempt >= attempts) {
            return;
        }
        long sleepMillis = (long) (Math.max(0.1D, properties.getRetrySleep() == null ? 2.0D : properties.getRetrySleep()) * 1000);
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}