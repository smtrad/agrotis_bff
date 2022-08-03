package br.com.agrotis.bff.service.v1;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.agrotis.bff.domain.Laboratorio;
import br.com.agrotis.core.service.AbstractService;

@Service("LaboratorioService-v1")
public class LaboratorioServiceV1 extends AbstractService<Laboratorio, UUID> {

}
