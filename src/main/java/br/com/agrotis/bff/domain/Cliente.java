package br.com.agrotis.bff.domain;


import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import br.com.agrotis.core.domain.AbstractEntity;
import br.com.agrotis.core.domain.ModelView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "cliente", schema = "agrotis", uniqueConstraints = {
		@UniqueConstraint(name = "cliente_nome", columnNames = {"nome"})
	})
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(of = {"nome"}, callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)  
public class Cliente extends AbstractEntity<Cliente, UUID> {
    
	private static final long serialVersionUID = 1L;

	@NotBlank(groups = {ModelView.INSERT.class})
    @Column(nullable = false, length = 150)
	@JsonView({ModelView.GET.class, ModelView.FIND.class})
    private String nome;
    
	@NotNull(groups = {ModelView.INSERT.class})
	@Column(nullable = false)    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	@JsonView({ModelView.GET.class})
    private LocalDateTime dataInicial;

    @NotNull(groups = {ModelView.INSERT.class})
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonView({ModelView.GET.class})
    private LocalDateTime dataFinal;
    
    @NotNull(groups = {ModelView.INSERT.class})
    @JsonView({ModelView.GET.class})
    @JsonProperty("infosPropriedade")
    @JoinColumn(name = "id_propriedade", nullable = false)
    //@see : https://stackoverflow.com/questions/61656985/jackson-module-not-registered-after-update-to-spring-boot-2
    @ManyToOne(optional = false, fetch=FetchType.EAGER/*, cascade = CascadeType.ALL*/)    
    @JsonIgnoreProperties("links")
    private Propriedade propriedade;    
    
    @NotNull(groups = {ModelView.INSERT.class})
    @JsonView({ModelView.GET.class})
    @JsonProperty("laboratorio") 
    @JoinColumn(name = "id_laboratorio", nullable = false)
    //@see : https://stackoverflow.com/questions/61656985/jackson-module-not-registered-after-update-to-spring-boot-2
    @ManyToOne(optional = false, fetch=FetchType.EAGER/*, cascade = CascadeType.ALL*/)    
    @JsonIgnoreProperties("links")
    private Laboratorio laboratorio;
            
    //@Lob see: https://stackoverflow.com/questions/68851570/hibernate-lob-on-byte-causes-bad-value-for-type-long
    //@Basic(fetch = FetchType.LAZY)
    @Column(nullable = true, length = 500)    
    @JsonView({ModelView.GET.class})
    private String observacoes;
    
}
