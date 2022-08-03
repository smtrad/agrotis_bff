package br.com.agrotis.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@Component
@ConfigurationProperties("application")
@Data
public class ApplicationProperties {

    private String name;
    private String version;
    private Boolean hateoas;

}
