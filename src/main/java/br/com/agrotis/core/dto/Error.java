package br.com.agrotis.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import br.com.agrotis.core.domain.ModelView;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Error {
	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    @Builder.Default
    private String code = "0";

	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    @Builder.Default
    private String description = "";

	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    @Builder.Default
    private String message = "";	
	
	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    @Builder.Default
    private String cause = "";
	
	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    @Builder.Default
    private String causeDescription = "";
	
	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    @Builder.Default
    private String errorMessage = "";
}
