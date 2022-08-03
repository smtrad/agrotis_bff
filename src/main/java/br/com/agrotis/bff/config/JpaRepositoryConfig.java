package br.com.agrotis.bff.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @see https://github.com/Cosium/spring-data-jpa-entity-graph/blob/master/doc/MAIN.md
 */
@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
public class JpaRepositoryConfig {
}
