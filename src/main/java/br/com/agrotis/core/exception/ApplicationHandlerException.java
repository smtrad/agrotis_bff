package br.com.agrotis.core.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.agrotis.core.config.ApplicationProperties;
import br.com.agrotis.core.dto.AppResponse;
import br.com.agrotis.core.dto.Error;
import br.com.agrotis.core.dto.Info;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
//@Order(Ordered.LOWEST_PRECEDENCE)
public class ApplicationHandlerException extends ResponseEntityExceptionHandler {


	private static final String DEFAULT_MSG = "Não foi possível executar sua solicitação no momento.";
	private static final String INVALID_ARG_MSG = "Os parametros informados são inválidos.";
	private static final String DATA_INTEGRITY_MSG = "Esta operação viola a integridade dos dados.";
    
    @Autowired
    private ApplicationProperties appProperties;


	@ExceptionHandler(value = AppException.class)
    protected ResponseEntity<Object> handleAppException(AppException e, WebRequest request) {
       log.error(e.getMessage());
       HttpStatus status = e.getStatus();// != null ? e.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
       return handleExceptionInternal(e, AppResponse.builder()
    		.info(Info.builder().appName(appProperties.getName()).appVersion(appProperties.getVersion()).build())
			.error(Error.builder().
					code(String.valueOf(status.value())).
					description(status.getReasonPhrase()).
					message(DEFAULT_MSG).
					errorMessage(e.getMessage()).
					cause(e.getClass().getSimpleName()).
					causeDescription(e.getMessage()).
					build())
			.build(), new HttpHeaders(), status, request);
    }
	
	@ExceptionHandler(value = DataIntegrityViolationException.class)	
    protected ResponseEntity<Object> handleConstraintException(DataIntegrityViolationException e, WebRequest request) {
	       log.error(e.getMessage());
	       HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	       return handleExceptionInternal(e, AppResponse.builder()
	    		.info(Info.builder().appName(appProperties.getName()).appVersion(appProperties.getVersion()).build())
				.error(Error.builder().
						code(String.valueOf(status.value())).
						description(status.getReasonPhrase()).
						message(DATA_INTEGRITY_MSG).
						errorMessage(e.getMessage()).
						cause(e.getCause().getClass().getSimpleName()).
						causeDescription(e.getRootCause().getMessage()).
						build())
				.build(), new HttpHeaders(), status, request);
	    }
    
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleAnyException(Exception e, WebRequest request) {
       log.error(e.getMessage());
       HttpStatus status = HttpStatus.BAD_REQUEST;
       return handleExceptionInternal(e, AppResponse.builder()
    		.info(Info.builder().appName(appProperties.getName()).appVersion(appProperties.getVersion()).build())
			.error(Error.builder().
					code(String.valueOf(status.value())).
					description(status.getReasonPhrase()).
					errorMessage(e.getMessage()).message(DEFAULT_MSG).
					cause(e.getCause().getClass().getSimpleName()).
					causeDescription(e.getCause().getMessage()).
					build())
			.build(), new HttpHeaders(), status, request);
    }
    
    @Override
    //@ExceptionHandler(value = MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {		
    	log.error(e.getMessage());
        status = HttpStatus.PRECONDITION_FAILED;
        StringBuilder sb = new StringBuilder();        
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();            
            sb.append(fieldName);
            sb.append(" : ");
            sb.append(errorMessage);
            sb.append(".");
            sb.append("\n");
        });
        return  handleExceptionInternal(e, AppResponse.builder()
        		.info(Info.builder().appName(appProperties.getName()).appVersion(appProperties.getVersion()).build())
    			.error(Error.builder().
    					code(String.valueOf(status.value())).
    					description(status.getReasonPhrase()).    					
    					message(INVALID_ARG_MSG).
    					errorMessage(e.getMessage()).
    					cause(e.getClass().getSimpleName()).
    					causeDescription(sb.toString()).
    					build())
    			.build() , new HttpHeaders(), status, request);
	}
}