package br.com.agrotis.bff.resource;

import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agrotis.bff.domain.Propriedade;
import br.com.agrotis.bff.specification.SpecificationTemplate.PropriedadeSpec;
import br.com.agrotis.core.controller.AbstractController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({"/v{apiVersion}/propriedade"})
public class PropriedadeResource extends AbstractController<Propriedade, UUID, PropriedadeSpec> {
}
