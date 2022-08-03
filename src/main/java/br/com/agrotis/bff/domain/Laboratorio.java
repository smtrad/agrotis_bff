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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="laboratorio", schema = "agrotis", uniqueConstraints = {
		@UniqueConstraint(name = "nome_laboratorio_duplicado", columnNames = {"nome"})
	}
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(of = {"nome"}, callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value={}, allowSetters = true)
@Builder
public class Laboratorio extends AbstractEntity<Laboratorio, UUID> {
    
	private static final long serialVersionUID = 1L;

	@NotBlank(groups = {ModelView.INSERT.class}, message = "O nome do laboratório não foi informado")
    @Column(nullable = false, length = 150)
    @JsonView({ModelView.GET.class, ModelView.FIND.class, ModelView.INSERT.class, ModelView.UPDATE.class})
    private String nome;               	

}
