package br.com.agrotis.core.controller;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import br.com.agrotis.core.dto.Info.InfoBuilder;
import br.com.agrotis.core.exception.AppException;
import br.com.agrotis.core.service.AbstractService;
import br.com.agrotis.core.utils.BeanProducer;
import br.com.agrotis.core.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractController<MODEL extends AbstractEntity<?,MODEL_ID>, MODEL_ID extends Serializable, SPEC extends Specification<MODEL>>  {
			
	@Autowired
	private BeanProducer producer;	

    @Autowired
    private ApplicationProperties appProperties;

    @Autowired
    private PagedResourcesAssembler<MODEL> pagedResourcesAssembler;              

    private final String MODEL_QUALIFIER = ReflectionUtils.getParameterizedType(this.getClass(), 0).getSimpleName();
    private final String SERVICE_QUALIFIER = MODEL_QUALIFIER.concat(AbstractService.SERVICE_SUFIX_QUALIFIER);
    
    @SuppressWarnings("unchecked")
	protected AbstractService<MODEL, MODEL_ID> getService(Integer apiVersion) {       	
 		return producer.getInstance(AbstractService.class,  apiVersion, SERVICE_QUALIFIER)
 				.orElseThrow(() -> new AppException("Serviço não disponível nesta versão : ".concat(SERVICE_QUALIFIER), null, HttpStatus.NOT_IMPLEMENTED));
    }	
		
	protected Info getInfo() {
		return getInfo(null);
	}
	
	protected Info getInfo(Page<?> page) {
 		InfoBuilder builder = Info.builder();
 		if (Objects.nonNull(page)) {
 			builder.page(page.getNumber()).
            size(page.getSize()).
            hasNext(page.hasNext());
 		}
		return builder.
        appName(appProperties.getName()).
        appVersion(appProperties.getVersion()).
        build();
	}
	
	protected <T> AppResponse<T> getResponse(T body) {
		AppResponse.AppResponseBuilder<T> builder = AppResponse.builder();
		AppResponse<T> payload = builder.info(getInfo()).content(body).build();
		return payload;
	}
	
	protected <T> ResponseEntity<AppResponse<T>> asOK(T body){
		AppResponse<T> payload = getResponse(body);
		return ResponseEntity.status(HttpStatus.OK).body(payload);
	}	
	
	protected ResponseEntity<AppResponse<List<MODEL>>> asOK(Page<MODEL> page, Integer apiInteger){
		AppResponse.AppResponseBuilder<List<MODEL>> builder = AppResponse.builder();
		AppResponse<List<MODEL>> payload = builder
				.info(getInfo(page))				
				.content(page.toList())
				.build();
		payload.add(hateos(page, apiInteger));
		return ResponseEntity.status(HttpStatus.OK).body(payload);
	}
	
	protected int getVersion(Integer apiVersion){
		return Optional.ofNullable(apiVersion).orElse(1);
	}
		
	protected Links hateos(Page<MODEL> page, Integer apiVersion){
		if (appProperties.getHateoas().booleanValue()) {
        	PagedModel<EntityModel<MODEL>> pageResult = pagedResourcesAssembler.toModel(page);            
            for (MODEL model : page.toList()) {
                try {
					model.add(WebMvcLinkBuilder.linkTo(this.getClass(),
					        this.getClass().getMethod("get", Integer.class, Serializable.class),
					        apiVersion,
					        model.getId()).withSelfRel());
				} catch (NoSuchMethodException | SecurityException e) {
					log.error("Falha ao mapear metainfo (hateos): {}", e.getMessage());
				}
            }
            return pageResult.getLinks();
        }
		return Links.NONE;
	}

    @GetMapping("/{id}")
    @JsonView({ModelView.GET.class})
    public ResponseEntity<AppResponse<MODEL>> get(
            @PathVariable(value = "apiVersion")  Integer apiVersion,
            @PathVariable(value = "id")  MODEL_ID id) {    	
    	log.info("get {} id: {}", MODEL_QUALIFIER, id);
    	apiVersion = getVersion(apiVersion);
    	AbstractService<MODEL, MODEL_ID> service = getService(apiVersion);    	
        MODEL model = service.get(apiVersion, id);
        log.info("found {} : {}", id, model);        
        return asOK(model);
    }

    @GetMapping
    @JsonView({ModelView.FIND.class})
    public ResponseEntity<AppResponse<List<MODEL>>> find(
            @PathVariable(value = "apiVersion")  Integer apiVersion,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
            ,SPEC modelSpecificationTemplate            
    ) throws NoSuchMethodException {
    	log.info("find {} : {} , {}", MODEL_QUALIFIER, modelSpecificationTemplate, pageable);
    	apiVersion = getVersion(apiVersion);
    	AbstractService<MODEL, MODEL_ID> service = getService(apiVersion);
        Page<MODEL> page = service.find(apiVersion, pageable, modelSpecificationTemplate);        
        return asOK(page,apiVersion);                                
    }
        

    @PostMapping
    @JsonView({ModelView.GET.class})
    public ResponseEntity<AppResponse<MODEL>> insert(
            @PathVariable(value = "apiVersion")  Integer apiVersion,
            @RequestBody @Validated({ModelView.INSERT.class}) MODEL model) {
    	log.info("insert {}", MODEL_QUALIFIER);    	
    	apiVersion = getVersion(apiVersion);
    	AbstractService<MODEL, MODEL_ID> service = getService(apiVersion);
    	service.insert(apiVersion, model);
		log.info("inserted {} : {}", MODEL_QUALIFIER, model.getId());
		return asOK(model);
    }

	@PutMapping("/{id}")
    @JsonView({ModelView.GET.class})
    public ResponseEntity<AppResponse<MODEL>> update(
            @PathVariable(value = "apiVersion")  Integer apiVersion,
            @PathVariable(value = "id")  MODEL_ID id,
            @RequestBody @Validated({ModelView.UPDATE.class}) MODEL source){
    	log.info("update {} : {}", MODEL_QUALIFIER, id);    	
    	apiVersion = getVersion(apiVersion);
    	AbstractService<MODEL, MODEL_ID> service = getService(apiVersion);
    	MODEL target = service.update(apiVersion, id, source);
		log.info("updated {} : {}", MODEL_QUALIFIER, id);
		return asOK(target);    	
    }

    @DeleteMapping("/{id}")
    @JsonView({ModelView.GET.class})
    public ResponseEntity<AppResponse<MODEL>> remove(
            @PathVariable(value = "apiVersion")  Integer apiVersion,
            @PathVariable(value = "id")  MODEL_ID id) {
    	log.info("remove {} : {}", MODEL_QUALIFIER, id);    	
    	apiVersion = getVersion(apiVersion);
    	AbstractService<MODEL, MODEL_ID> service = getService(apiVersion);
    	MODEL target = service.remove(apiVersion, id);
		log.info("removed {} : {}", MODEL_QUALIFIER, id);
		return asOK(target);
    }

}
