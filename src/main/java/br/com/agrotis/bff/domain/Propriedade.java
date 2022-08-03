package br.com.agrotis.bff.domain;


import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import br.com.agrotis.core.domain.AbstractEntity;
import br.com.agrotis.core.domain.ModelView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name="propriedade", schema = "agrotis", uniqueConstraints = {
		@UniqueConstraint(name = "cnpj_propriedade_duplicado", columnNames = {"cnpj"})
	})
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(of = {"cnpj","nome"}, callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value={}, allowSetters = true)
public class Propriedade extends AbstractEntity<Propriedade, UUID> {
    
	private static final long serialVersionUID = 1L;

	@NotBlank(groups = {ModelView.INSERT.class})
    @Column(nullable = false, length = 150)
	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private String nome;       
        
	@NotBlank(groups = {ModelView.INSERT.class})
	@Column(nullable = false, length = 14)
	@JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private String cnpj;

}
