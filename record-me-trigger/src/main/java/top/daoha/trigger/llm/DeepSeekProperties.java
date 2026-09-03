package top.daoha.trigger.llm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "llm.deepseek")
public class DeepSeekProperties {

    private String model = "deepseek-v4-flash";

    private String baseUrl = "https://api.deepseek.com";

    private String apiKey;

    private Integer timeout = 120;

    private Integer maxRetries = 3;

    private Double retrySleep = 2.0D;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Double getRetrySleep() {
        return retrySleep;
    }

    public void setRetrySleep(Double retrySleep) {
        this.retrySleep = retrySleep;
    }
}
