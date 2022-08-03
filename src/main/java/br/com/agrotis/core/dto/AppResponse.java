package br.com.agrotis.core.dto;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import br.com.agrotis.core.domain.ModelView;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppResponse<T> extends RepresentationModel<AppResponse<?>> {
	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private T content;

	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private Error error;

	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private Info info;

}
