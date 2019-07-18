package com.lephix.easy.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "easy")
@Data
public class EasyProperties {

    private Mvc mvc = new Mvc();
    private Security security = new Security();

    @Data
    public static class Mvc {
        private boolean rewriteResponse = true;
        private boolean rewriteErrorResponse = true;
    }

    @Data
    public static class Security {
        private String source;
    }

}
