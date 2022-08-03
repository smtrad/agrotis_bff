package br.com.agrotis.core.utils;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.agrotis.core.utils.BeanProducer;


@RunWith(MockitoJUnitRunner.class)
public class BeanProducerTest {
	
	@InjectMocks
	private BeanProducer producer;
	
	@Mock
	private BeanFactory beanFactory;
	
	@SuppressWarnings("unchecked")
	@Test
   	public void getInstance_sucesso() 
   			throws JsonProcessingException {   
		when(beanFactory.getBean(any(Exception.class.getClass())))
			.thenReturn(new Exception());
    	Optional<Exception> instance = producer.getInstance(Exception.class);
		assertTrue(instance.isPresent());   			       	
   	} 
			
	@SuppressWarnings("unchecked")
	@Test
   	public void getInstance_type_and_qualifier_sucesso() 
   			throws JsonProcessingException {   
		when(beanFactory.getBean(anyString(), any(Exception.class.getClass())))
			.thenReturn(new Exception());		
    	Optional<Exception> instance = producer.getInstance(Exception.class, 1, "Exception");
		assertTrue(instance.isPresent());   			       	
   	} 	

	
	@SuppressWarnings("unchecked")
	@Test
   	public void getInstanceQualificado_sucesso() 
   			throws JsonProcessingException {   		
		when(beanFactory.getBean(anyString(), any(Exception.class.getClass())))
			.thenReturn(new Exception());
    	Optional<Exception> instance = producer.getInstance(Exception.class, "teste");
		assertTrue(instance.isPresent());   			       	
   	}
		
	@Test
   	public void getInstance_bean_nao_encontrado() 
   			throws JsonProcessingException {   	
    	Optional<Exception> instance = producer.getInstance(Exception.class, "teste");
    	assertTrue(!instance.isPresent());		   			       
   	}
		
	@Test
   	public void getInstance_bean_nao_corresponde_ao_tipo() 
   			throws JsonProcessingException {   		
    	Optional<Exception> instance = producer.getInstance(Exception.class, "teste");
    	assertTrue(!instance.isPresent());		   			       
   	}	
		
	@SuppressWarnings("unchecked")
	@Test
   	public void getInstance_nao_versionado_sucesso() 
   			throws JsonProcessingException {   
		when(beanFactory.getBean(any(Exception.class.getClass())))
			.thenReturn(new Exception());
    	Optional<Exception> instance = producer.getInstance(Exception.class, 0);		
		assertTrue(instance.isPresent());   			       	
   	} 
		
	@SuppressWarnings("unchecked")
	@Test
   	public void getInstance_versionado_sucesso() 
   			throws JsonProcessingException {   		
		when(beanFactory.getBean(anyString(), any(Exception.class.getClass())))
			.thenReturn(new Exception());		
    	Optional<Exception> instance = producer.getInstance(Exception.class, 1);
		assertTrue(instance.isPresent());   			       	
   	} 
		
	@SuppressWarnings("unchecked")
	@Test
   	public void getAnyInstance_sucesso() 
   			throws JsonProcessingException {   		
		when(beanFactory.getBean(anyString(), any(Exception.class.getClass())))
			.thenThrow(new NoSuchBeanDefinitionException(""))
			.thenThrow(new NoSuchBeanDefinitionException(""))
			.thenThrow(new NoSuchBeanDefinitionException(""))
			.thenReturn(new Exception());
    	Optional<Exception> instance = producer.getAnyInstance(Exception.class, 2,"teste01","teste02");
		assertTrue(instance.isPresent());   			       	
   	}
		
	@Test
   	public void getAnyInstance_bean_nao_encontrado() 
   			throws JsonProcessingException {   		
    	Optional<Exception> instance = producer.getAnyInstance(Exception.class, 2, "teste01", "teste02");
    	assertTrue(!instance.isPresent());		   			       
   	}
	
	@Test
   	public void getAnyInstance_nao_versionado_sucesso() 
   			throws JsonProcessingException {   
    	Optional<Exception> instance = producer.getAnyInstance(Exception.class, 0, "teste01", "teste02");
		assertTrue(!instance.isPresent());   			       	
   	} 
	
	@Test
   	public void getAnyInstance_qualificadores_invalidos_sucesso() 
   			throws JsonProcessingException {   
    	Optional<Exception> instance = producer.getAnyInstance(Exception.class, 1, "", "");
		assertTrue(!instance.isPresent());   			       	
   	} 
	
}
