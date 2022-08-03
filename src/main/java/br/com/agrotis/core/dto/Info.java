package br.com.agrotis.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import br.com.agrotis.core.domain.ModelView;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Info {

	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private String appName;

	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private String appVersion;

	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private Integer page;

	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private Integer size;

	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private Boolean hasNext;

}
