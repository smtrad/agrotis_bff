package br.com.agrotis.bff.resource;

import java.util.Optional;
import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrotis.bff.domain.Cliente;
import br.com.agrotis.bff.specification.SpecificationTemplate.ClienteSpec;
import br.com.agrotis.core.controller.AbstractController;
import br.com.agrotis.core.domain.ModelView;
import br.com.agrotis.core.dto.AppResponse;
import br.com.agrotis.core.exception.AppException;
import br.com.agrotis.core.utils.EntityValidator;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({"/v{apiVersion}/cliente"})
public class ClienteResource extends AbstractController<Cliente, UUID, ClienteSpec> {
	
	private static final String MSG_SUCESSO = "Cadastro realizado com sucesso";
	private static final String MSG_ERRO = "Preencha os campos obrigatórios.";
	
	@Autowired
	private EntityValidator validator;	
	
	private Object onViolate(ConstraintViolationException cause) {
		throw new AppException(MSG_ERRO, cause, HttpStatus.PRECONDITION_FAILED);
	}

	@PostMapping("/salvar")
	public ResponseEntity<AppResponse<String>> salvar(
			@PathVariable(value = "apiVersion")  Integer apiVersion,
            @RequestBody Cliente model) {
		Optional<UUID> id = Optional.ofNullable(model.getId());
		if (id.isPresent()) {			
			validator.validate(model, ModelView.UPDATE.class, this::onViolate);
			super.update(apiVersion, id.get(), model);
		}else {
			validator.validate(model, ModelView.INSERT.class, this::onViolate);
			super.insert(apiVersion, model);
		}
		return asOK(MSG_SUCESSO);
	}	
	
}
