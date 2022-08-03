package br.com.agrotis.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableJpaRepositories
//@EnableWebMvc
/**
 * @see: https://www.baeldung.com/spring-httpmessageconverter-rest
 * @see: https://www.baeldung.com/web-mvc-configurer-adapter-deprecated
 * @see: https://github.com/FasterXML/jackson-datatype-hibernate
 * @see: https://programmer.group/solution-of-spring-boot-jpa-entity-jackson-serialization-triggering-lazy-load.html
 *
 */
public class WebConfig 
	//extends WebMvcConfigurerAdapter 
	implements WebMvcConfigurer {
			 
	
/*	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private MappingJackson2HttpMessageConverter converter;
	
	
	@Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(new MappingJackson2JsonView());
        registry.jsp();
    }
	
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter);
    }
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {	 		
	    for (HttpMessageConverter<?> c : converters){
	        if (c instanceof MappingJackson2HttpMessageConverter) {
	            ((AbstractJackson2HttpMessageConverter) c).setObjectMapper(mapper);
	        }
	    }
	}	
*/
}
