package br.com.agrotis.bff.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.agrotis.bff.domain.Cliente;
import br.com.agrotis.core.repository.GenericRepository;

@Repository
public interface ClienteRepository extends GenericRepository<Cliente, UUID> {

  
}
