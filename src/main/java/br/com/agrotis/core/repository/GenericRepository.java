package br.com.agrotis.core.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.agrotis.core.domain.AbstractEntity;

public interface GenericRepository<MODEL extends AbstractEntity<?, ?>, MODEL_ID extends Serializable>
        extends JpaRepository<MODEL, MODEL_ID>, JpaSpecificationExecutor<MODEL>
        //EntityGraphJpaRepository<MODEL, MODEL_ID>, EntityGraphJpaSpecificationExecutor<MODEL> 
        {
}
