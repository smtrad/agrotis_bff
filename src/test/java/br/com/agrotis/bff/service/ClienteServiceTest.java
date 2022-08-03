package br.com.agrotis.bff.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.com.agrotis.bff.domain.Cliente;
import br.com.agrotis.bff.repository.ClienteRepository;
import br.com.agrotis.bff.resource.ClienteResourceTest;
import br.com.agrotis.bff.service.v1.ClienteServiceV1;
import br.com.agrotis.bff.specification.SpecificationTemplate.ClienteSpec;
@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {
	
	private static final Integer VERSAO_API = 1;
	  
    @InjectMocks
    private ClienteServiceV1 service;
    
    @Mock
    protected EntityManager entityManager;
	
    @Mock
    protected ClienteRepository repository;
    
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void inserir() {
		Cliente cliente = ClienteResourceTest.build(null);
		when(entityManager.find(any(), any()))			
			.thenReturn(cliente.getPropriedade())
			.thenReturn(cliente.getLaboratorio())
			.thenReturn(cliente)
			;
		Cliente response = service.insert(VERSAO_API, cliente);	
		assertNotNull(response);
	}
	
	@Test
	public void atualizar() {
		UUID id = UUID.fromString("f95fe67c-ba08-48fd-9677-1148c83ae9bb");
		Cliente cliente = ClienteResourceTest.build(id);
		when(entityManager.find(any(), any()))
			.thenReturn(cliente)
			.thenReturn(cliente.getPropriedade())
			.thenReturn(cliente.getLaboratorio())
			;
		Cliente response = service.update(VERSAO_API, id, cliente);	
		assertNotNull(response);
	}
	
	@Test
	public void remover() {
		UUID id = UUID.fromString("f95fe67c-ba08-48fd-9677-1148c83ae9bb");
		Cliente cliente = ClienteResourceTest.build(id);
		when(repository.findById(any()))
			.thenReturn(Optional.of(cliente))
			;
		Cliente response = service.remove(VERSAO_API, id);	
		assertNotNull(response);
	}
	
	@Test
	public void get() {
		UUID id = UUID.fromString("f95fe67c-ba08-48fd-9677-1148c83ae9bb");
		Cliente cliente = ClienteResourceTest.build(id);
		when(repository.findById(any()))
			.thenReturn(Optional.of(cliente))
			;
		Cliente response = service.get(VERSAO_API, id);	
		assertNotNull(response);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void find() {
		Cliente cliente = ClienteResourceTest.build(UUID.fromString("f95fe67c-ba08-48fd-9677-1148c83ae9bb"));
		
		List<Cliente> clientes =  new ArrayList<>();
		clientes.add(cliente);
		
		ClienteSpec spec = new ClienteSpec() {
			@Override
			public Predicate toPredicate(Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {			
				return null;
			}
			
		};
		Sort sort = Sort.unsorted();
		Pageable pageable = PageRequest.of(1, 1, sort);
		Page<Cliente> page = new PageImpl<>(clientes, pageable, clientes.size()); 			
		when(repository.findAll(any(spec.getClass()), any(Pageable.class)))
			.thenReturn(page)
			;
		Page<Cliente> response = service.find(VERSAO_API, pageable , spec);
		assertNotNull(response);
	}

}
