package br.com.agrotis.core.service;


import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.agrotis.core.domain.AbstractEntity;
import br.com.agrotis.core.exception.AppException;
import br.com.agrotis.core.repository.GenericRepository;
import br.com.agrotis.core.utils.JsonViewUtils;
import br.com.agrotis.core.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractService<MODEL extends AbstractEntity<?,MODEL_ID>, MODEL_ID extends Serializable>  {
		
	@Autowired
    protected EntityManager entityManager;
	
	@Autowired
    protected GenericRepository<MODEL, MODEL_ID> repository;
      

    @SuppressWarnings("unchecked")
	private <T extends AbstractEntity<?,?>> T  manageEntity(T entity){
    	if ((Objects.nonNull(entity))&&(Objects.nonNull(entity.getId()))) {
    		return //entityManager.merge(entity);
    				(T) entityManager.find(entity.getClass(), entity.getId());
    	}
    	return entity;
    }
    
    public MODEL get(Integer apiVersion, MODEL_ID id) {
    	log.info("get id: {}", id);
        MODEL model = repository.findById(id).orElseThrow(() -> new AppException("Registro não encontrado", null, HttpStatus.NOT_FOUND));
        log.info("found {} : {}", id, model);
        return model;
    }

    public Page<MODEL> find(Integer apiVersion, Pageable pageable, Specification<MODEL> modelSpecificationTemplate) {
    	log.info("find with: {}", pageable);
        Page<MODEL> page = repository.findAll(modelSpecificationTemplate, pageable);        
        return page;
    }        
   
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MODEL insert(Integer apiVersion,MODEL source) {
    	log.info("insert : {}", source);    	
    	ReflectionUtils.updateValueOfType(source, AbstractEntity.class, this::manageEntity);
    	entityManager.persist(source);
		log.info("inserted : {}", source);
		return source;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MODEL update(Integer apiVersion, MODEL_ID id, MODEL source) {
    	log.info("update : {} as {}", id, source);
    	source.setId(id);
    	MODEL target = Optional.ofNullable(manageEntity(source))
    			.orElseThrow(() -> new AppException("Registro não encontrado com id:".concat(id.toString()), null, HttpStatus.NOT_FOUND));
    	ReflectionUtils.updateValueOfType(source, AbstractEntity.class, this::manageEntity);    	
    	BeanUtils.copyProperties(source, target, JsonViewUtils.getNullPropertyNames(source));
    	log.info("updated : {}", target); 
        return target;
    }

    public MODEL remove(Integer apiVersion, MODEL_ID id){
    	log.info("remove : {}", id);    		
		MODEL target = repository.findById(id)
    			.orElseThrow(() -> new AppException("Registro não encontrado com id:".concat(id.toString()), null, HttpStatus.NOT_FOUND));
		repository.delete(target);
		log.info("removed : {}", id);
		return target;
    }

}
