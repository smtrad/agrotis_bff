package br.com.agrotis.bff.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.agrotis.bff.domain.Cliente;
import br.com.agrotis.bff.domain.Laboratorio;
import br.com.agrotis.bff.domain.Propriedade;
import br.com.agrotis.bff.service.v1.ClienteServiceV1;
import br.com.agrotis.bff.specification.SpecificationTemplate.ClienteSpec;
import br.com.agrotis.core.config.ApplicationProperties;
import br.com.agrotis.core.dto.AppResponse;
import br.com.agrotis.core.exception.AppException;
import br.com.agrotis.core.service.AbstractService;
import br.com.agrotis.core.utils.BeanProducer;
import br.com.agrotis.core.utils.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class ClienteResourceTest {
	
	private static final Integer VERSAO_API = 1;
		  
    @InjectMocks
    private ClienteResource resouce;
    
    @Mock
	private ClienteServiceV1 service;        	
    
    @Mock
	private BeanProducer producer;	
    
    @Spy
    private ValidatorFactory factory = spy(Validation.buildDefaultValidatorFactory());
    
    @Spy
    private EntityValidator validator;

    @Spy
    private ApplicationProperties appProperties;

    @Spy
    private PagedResourcesAssembler<Cliente> pagedResourcesAssembler =
    		spy(new PagedResourcesAssembler<Cliente>(
    				new HateoasPageableHandlerMethodArgumentResolver(), 
    				 UriComponentsBuilder.fromUriString("https://localhost:8080").build()
    				));
    
    
    public static Cliente build(UUID id) {    	 
    	
    	Laboratorio laboratorio = Laboratorio.builder()
    			.nome("Agrotech S/A")    			
    			.build();
    	laboratorio.setId(UUID.fromString("a865896b-7349-4a57-a052-65cb53af5cba"));
    	
		Propriedade propriedade = Propriedade.builder()
				.nome("Fazenda Boi Gordo")
				.cnpj("99999999999999")
				.build();
		propriedade.setId(UUID.fromString("6110f08b-9ee4-4734-9ede-bee846925e8d"));
		
		Cliente cliente = Cliente.builder()
    			.nome("Ricardo Vasconcelos")
    			.dataInicial(LocalDateTime.now())
    			.dataFinal(LocalDateTime.now())
    			.observacoes("Notes...")
    			.laboratorio(laboratorio)
    			.propriedade(propriedade)
    			.build();
		cliente.setId(id);
    	return cliente;
    }	
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		//Validator		
		ReflectionTestUtils.setField(validator, "factory", factory);
		//ApplicationProperties
		ReflectionTestUtils.setField(appProperties, "name", "app");
		ReflectionTestUtils.setField(appProperties, "version", "1.0.0");
		ReflectionTestUtils.setField(appProperties, "hateoas", true);
		//producer
		when(producer.getInstance(any(AbstractService.class.getClass()), anyInt(), anyString()))
			.thenReturn(Optional.of(service));
	}
	
	@Test
	public void inserir_validationError() {
		AppException e = assertThrows(AppException.class, () -> {
			Cliente cliente = build(null);
			cliente.setLaboratorio(null);
			resouce.salvar(VERSAO_API, cliente);
		});
		assertEquals(ConstraintViolationException.class, e.getCause().getClass());
	}
	
	@Test
	public void inserir() {
		Cliente cliente = build(null);
		when(service.insert(anyInt(), any(Cliente.class))).thenReturn(cliente);
   		ResponseEntity<AppResponse<String>> response = resouce.salvar(VERSAO_API, cliente);	
		assertEquals(200, response.getStatusCode().value());		
	}
	
	@Test
	public void atualizar() {
		UUID id = UUID.fromString("f95fe67c-ba08-48fd-9677-1148c83ae9bb");
		Cliente cliente = build(id);
		when(service.update(anyInt(), any(UUID.class), any(Cliente.class))).thenReturn(cliente);
   		ResponseEntity<AppResponse<String>> response = resouce.salvar(VERSAO_API, cliente);	
		assertEquals(200, response.getStatusCode().value());
	}
	
	@Test
	public void remover() {
		UUID id = UUID.fromString("f95fe67c-ba08-48fd-9677-1148c83ae9bb");
		Cliente cliente = build(id);
		when(service.remove(anyInt(), any(UUID.class))).thenReturn(cliente);
   		ResponseEntity<AppResponse<Cliente>> response = resouce.remove(VERSAO_API, id);	
		assertEquals(200, response.getStatusCode().value());
	}
	
	@Test
	public void get() {
		UUID id = UUID.fromString("f95fe67c-ba08-48fd-9677-1148c83ae9bb");
		Cliente cliente = build(id);
		when(service.get(anyInt(), any(UUID.class))).thenReturn(cliente);
   		ResponseEntity<AppResponse<Cliente>> response = resouce.get(VERSAO_API, id);	
		assertEquals(200, response.getStatusCode().value());
	}
	
	@Test
	public void find() {
		
		List<Cliente> clientes =  new ArrayList<>();
		clientes.add(build(UUID.fromString("f95fe67c-ba08-48fd-9677-1148c83ae9bb")));
		
		ClienteSpec spec = null;
		Sort sort = Sort.unsorted();
		Pageable pageable = PageRequest.of(1, 1, sort);
		Page<Cliente> page = new PageImpl<>(clientes, pageable, clientes.size()); 				
		
		when(service.find(anyInt(), any(Pageable.class), any())).thenReturn(page);
		ResponseEntity<AppResponse<List<Cliente>>> response = resouce.find(VERSAO_API, pageable , spec);	
		assertEquals(200, response.getStatusCode().value());
	}
	
	@Test
	public void find_withDisableHATEOAS() {
		ReflectionTestUtils.setField(appProperties, "hateoas", false);
		
		List<Cliente> clientes =  new ArrayList<>();
		clientes.add(build(UUID.fromString("f95fe67c-ba08-48fd-9677-1148c83ae9bb")));
		
		ClienteSpec spec = null;
		Sort sort = Sort.unsorted();
		Pageable pageable = PageRequest.of(1, 1, sort);
		Page<Cliente> page = new PageImpl<>(clientes, pageable, clientes.size()); 				
		
		when(service.find(anyInt(), any(Pageable.class), any())).thenReturn(page);
		ResponseEntity<AppResponse<List<Cliente>>> response = resouce.find(VERSAO_API, pageable , spec);	
		assertEquals(200, response.getStatusCode().value());
	}		
	

}
