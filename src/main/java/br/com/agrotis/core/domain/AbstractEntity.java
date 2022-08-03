package br.com.agrotis.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@MappedSuperclass
@Data
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id"})
public abstract class AbstractEntity<MODEL extends AbstractEntity<MODEL, ID>, ID extends Serializable>
        extends RepresentationModel<MODEL> 
		implements Serializable {
    
	private static final long serialVersionUID = 1L;	

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({ModelView.ID.class})	
    private ID id;

    @Version
    @Column(nullable = false)
    @JsonView({ModelView.GET.class})
    private Integer version;    

  
}
