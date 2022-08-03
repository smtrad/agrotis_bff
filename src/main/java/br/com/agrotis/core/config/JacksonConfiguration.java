package br.com.agrotis.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;


/**
 * @see https://stackoverflow.com/questions/21708339/avoid-jackson-serialization-on-non-fetched-lazy-objects
 * @see https://github.com/FasterXML/jackson-datatype-hibernate
 * 
 * Module Error
 * @see : https://stackoverflow.com/questions/61656985/jackson-module-not-registered-after-update-to-spring-boot-2
 * @see : https://stackoverflow.com/questions/71419165/spring-seems-to-be-ignoring-hibernate5module-and-lazy-loading-of-hibernate-objec
 * 
 * @author smtrad
 * 
 * ESTÁ COM ERRO NO SPRING : REGISTRO DO MÓDULO SEM EFEITO.
 *
 */
@Configuration(proxyBeanMethods = false)
public class JacksonConfiguration {		

	protected Module module() {	    	  
	    return hibernate5Module();
	}

	public Hibernate5Module hibernate5Module() {
		//Hibernate5Module h5module = new Hibernate5Module(sf);
		Hibernate5Module h5module = new Hibernate5Module();
        //h5module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
	    //h5module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
		h5module.disable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
	    return h5module;
	}

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);       
        mapper.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.registerModule(module());       
        return mapper;
    } 
    
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.modules(module());
        builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        builder.featuresToDisable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        
        return builder;
    }
    
}