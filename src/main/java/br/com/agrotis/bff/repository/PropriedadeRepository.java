package br.com.agrotis.bff.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.agrotis.bff.domain.Propriedade;
import br.com.agrotis.core.repository.GenericRepository;

@Repository
public interface PropriedadeRepository extends GenericRepository<Propriedade, UUID> {

  
}
