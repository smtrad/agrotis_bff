package br.com.agrotis.bff.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.agrotis.bff.domain.Laboratorio;
import br.com.agrotis.core.repository.GenericRepository;

@Repository
public interface LaboratorioRepository extends GenericRepository<Laboratorio, UUID> {

  
}
