package br.com.agrotis.core.controller;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.agrotis.core.config.ApplicationProperties;
import br.com.agrotis.core.domain.AbstractEntity;
import br.com.agrotis.core.domain.ModelView;
import br.com.agrotis.core.dto.AppResponse;
import br.com.agrotis.core.dto.Info;
import br.com.agrotis.core.exception.AppException;
import br.com.agrotis.core.repository.GenericRepository;
import br.com.agrotis.core.utils.JsonViewUtils;
import br.com.agrotis.core.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractController<MODEL extends AbstractEntity<?,MODEL_ID>, MODEL_ID extends Serializable, SPEC extends Specification<MODEL>>  {
	
	

	@Autowired
    private EntityManager entityManager;
	
	@Autowired
    private GenericRepository<MODEL, MODEL_ID> repository;

    @Autowired
    private ApplicationProperties appProperties;

    @Autowired
    private PagedResourcesAssembler<MODEL> pagedResourcesAssembler;
      

    @GetMapping("/{id}")
    @JsonView({ModelView.GET.class})
    public ResponseEntity<AppResponse<MODEL>> get(
            @PathVariable(value = "apiVersion")  Integer apiVersion,
            @PathVariable(value = "id")  MODEL_ID id) {
    	log.info("get id: {}", id);
        MODEL model = repository.findById(id).orElseThrow(() -> new AppException("Registro não encontrado", null, HttpStatus.NOT_FOUND));
        log.info("found {} : {}", id, model);
        AppResponse.AppResponseBuilder<MODEL> builder = AppResponse.builder();
        return ResponseEntity.status(HttpStatus.OK).
        		body(builder.
        				info(Info.builder().
                                appName(appProperties.getName()).
                                appVersion(appProperties.getVersion()).
                                build()).
        				content(model).build());
    }

    @GetMapping
    @JsonView({ModelView.FIND.class})
    public ResponseEntity<AppResponse<List<MODEL>>> find(
            @PathVariable(value = "apiVersion")  Integer apiVersion,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
            ,SPEC modelSpecificationTemplate            
    ) throws NoSuchMethodException {
    	log.info("find with: {}", pageable);
        Page<MODEL> page = repository.findAll(modelSpecificationTemplate, pageable);
        
        AppResponse.AppResponseBuilder<List<MODEL>> builder = AppResponse.builder();
        AppResponse<List<MODEL>> response = builder.content(page.toList()).
                info(Info.builder().
                        appName(appProperties.getName()).
                        appVersion(appProperties.getVersion()).
                        page(page.getNumber()).
                        size(page.getSize()).
                        hasNext(page.hasNext()).
                        build()).build();
        
        if (appProperties.getHateoas().booleanValue()) {
        	PagedModel<EntityModel<MODEL>> pageResult = pagedResourcesAssembler.toModel(page);
            response.add(pageResult.getLinks());
            for (MODEL model : page.toList()) {
                model.add(WebMvcLinkBuilder.linkTo(this.getClass(),
                        this.getClass().getMethod("get", Integer.class, Serializable.class),
                        apiVersion,
                        model.getId()).withSelfRel());
            }
        }
        

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    private AbstractEntity<?,?> manageEntity(AbstractEntity<?,?> entity){
    	if ((Objects.nonNull(entity))&&(Objects.nonNull(entity.getId()))) {
    		return //entityManager.merge(entity);
    				entityManager.find(entity.getClass(), entity.getId());
    	}
    	return entity;
    }

    @PostMapping
    @JsonView({ModelView.GET.class})
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<AppResponse<MODEL>> insert(
            @PathVariable(value = "apiVersion")  Optional<Integer> apiVersion,
            @RequestBody 
            @Validated({ModelView.INSERT.class}) //@JsonView({ModelView.INSERT.class}) 
            MODEL source) {
    	log.info("insert : {}", source);    	
    	ReflectionUtils.updateValueOfType(source, AbstractEntity.class, this::manageEntity);
    	entityManager.persist(source);
		log.info("inserted : {}", source);
		AppResponse.AppResponseBuilder<MODEL> builder = AppResponse.builder();
        return ResponseEntity.status(HttpStatus.OK).
        		body(builder.
        				info(Info.builder().
                                appName(appProperties.getName()).
                                appVersion(appProperties.getVersion()).
                                build()).
        				content(source).build());
    }

    @SuppressWarnings("unchecked")
	@PutMapping("/{id}")
    @JsonView({ModelView.GET.class})
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<AppResponse<MODEL>> update(
            @PathVariable(value = "apiVersion")  Optional<Integer> apiVersion,
            @PathVariable(value = "id")  MODEL_ID id,
            @RequestBody @Validated({ModelView.UPDATE.class}) //@JsonView({ModelView.UPDATE.class})
            MODEL source){
    	log.info("update : {} as {}", id, source);
    	source.setId(id);
    	MODEL target = (MODEL) Optional.ofNullable(manageEntity(source))
    			.orElseThrow(() -> new AppException("Registro não encontrado com id:".concat(id.toString()), null, HttpStatus.NOT_FOUND));
    	ReflectionUtils.updateValueOfType(source, AbstractEntity.class, this::manageEntity);    	
    	BeanUtils.copyProperties(source, target, JsonViewUtils.getNullPropertyNames(source));
    	//target = repository.save(target);
    	//entityManager.flush();
    	log.info("updated : {}", target);
    	AppResponse.AppResponseBuilder<MODEL> builder = AppResponse.builder();
        return ResponseEntity.status(HttpStatus.OK).
        		body(builder.
        				info(Info.builder().
                                appName(appProperties.getName()).
                                appVersion(appProperties.getVersion()).
                                build()).
        				content(target).build());
    }

    @DeleteMapping("/{id}")
    @JsonView({ModelView.GET.class})
    public ResponseEntity<AppResponse<MODEL>> remove(
            @PathVariable(value = "apiVersion")  Optional<Integer> apiVersion,
            @PathVariable(value = "id")  MODEL_ID id){
    	log.info("remove : {}", id);    		
		MODEL target = repository.findById(id)
    			.orElseThrow(() -> new AppException("Registro não encontrado com id:".concat(id.toString()), null, HttpStatus.NOT_FOUND));
		repository.delete(target);
		log.info("removed : {}", id);
		AppResponse.AppResponseBuilder<MODEL> builder = AppResponse.builder();
        return ResponseEntity.status(HttpStatus.OK).
        		body(builder.
        				info(Info.builder().
                                appName(appProperties.getName()).
                                appVersion(appProperties.getVersion()).
                                build()).
        				content(target).build());
    }

}
