package com.lunartown.zzimcong.user.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@ConfigurationProperties(prefix = "email")
@Validated
@Data
public class EmailProperties {
    @NotBlank
    private String host;

    @Min(1)
    @Max(65535)
    private int port;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Properties properties = new Properties();

    @Data
    public static class Properties {
        private Mail mail = new Mail();

        @Data
        public static class Mail {
            private Smtp smtp = new Smtp();

            @Data
            public static class Smtp {
                private boolean auth;
                private Starttls starttls = new Starttls();

                @Data
                public static class Starttls {
                    private boolean enable;
                }
            }
        }
    }

    @NotBlank
    private String sender;

    @NotBlank
    private String subject;

    private Template template = new Template();
    private Verification verification = new Verification();

    @Data
    public static class Template {
        @NotBlank
        private String path;
    }

    @Data
    public static class Verification {
        private Code code = new Code();
        private Token token = new Token();

        @Data
        public static class Code {
            @Min(4)
            @Max(10)
            private int length = 6;

            private Expiry expiry = new Expiry();

            @Data
            public static class Expiry {
                @Min(60)
                @Max(600)
                private long seconds = 300;
            }
        }

        @Data
        public static class Token {
            private Expiry expiry = new Expiry();

            @Data
            public static class Expiry {
                @Min(5)
                @Max(60)
                private long minutes = 30;
            }
        }
    }
}