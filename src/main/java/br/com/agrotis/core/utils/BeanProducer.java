package br.com.agrotis.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class BeanProducer {

    private static final String TOKEN_SEPARATOR = "-";
    private static final String VERSION_PREFIX = "v";

    @Autowired
    private BeanFactory beanFactory;

    public <T> Optional<T> getInstance(Class<T> type) {
        return getInstance(type, "");
    }

    public <T> Optional<T> getInstance(Class<T> type, String qualifier) {
        Optional<T> bean = Optional.empty();
        try {
            bean = Optional.ofNullable(Strings.isBlank(qualifier) ? beanFactory.getBean(type) : beanFactory.getBean(qualifier, type));
        }catch(NoSuchBeanDefinitionException e) {
            log.debug("Been qualificado como {} não encontrado", qualifier);
        }catch(BeanNotOfRequiredTypeException e) {
            log.debug("Been qualificado como {} não corresponde ao tipo esperado", qualifier);
        }
        return bean;
    }

    public <T> Optional<T> getInstance(Class<T> type, Integer version) {
        return getInstance(type, version, null);
    }

    public <T> Optional<T> getInstance(Class<T> type, Integer version, String qualifier) {
        Optional<T> bean = Optional.empty();
        if (version.intValue() == 0) //Sem controle de versão
            return getInstance(type, qualifier);
        if (Strings.isBlank(qualifier)) //Controle de versão exige qualificador
            qualifier = type.getSimpleName();
        int versao = version;
        while ((!bean.isPresent())&&(versao > 0)) {
            bean = getInstance(type, getNamedVersionQualifier(versao--, qualifier));
        }
        return bean;
    }

    public <T> Optional<T> getAnyInstance(Class<T> type, Integer version, String... qualifier) {
        Optional<T> bean = Optional.empty();
        if (version.intValue() == 0) {
            return bean;
        }
        int versao = version;
        while ((!bean.isPresent())&&(versao > 0)) {
            for (String qualifierPrefix : qualifier) {
                if (!Strings.isBlank(qualifierPrefix)) {
                    bean = getInstance(type, getNamedVersionQualifier(versao, qualifierPrefix));
                    if (bean.isPresent()) {
                        break;
                    }
                }
            }
            --versao;
        }
        return bean;
    }

    private String getNamedVersionQualifier(Integer version, String qualifier) {
        StringBuilder sb = new StringBuilder();
        if (!Strings.isBlank(qualifier)) {
            sb.append(qualifier);
            sb.append(TOKEN_SEPARATOR);
        }
        sb.append(VERSION_PREFIX);
        sb.append(version.toString());
        return sb.toString();
    }

}