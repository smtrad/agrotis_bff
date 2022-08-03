package br.com.agrotis.bff.specification;



import org.springframework.data.jpa.domain.Specification;

import br.com.agrotis.bff.domain.Cliente;
import br.com.agrotis.bff.domain.Laboratorio;
import br.com.agrotis.bff.domain.Propriedade;
import br.com.agrotis.core.domain.AbstractEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationTemplate {
    @And({
            @Spec(path = "id", spec = GreaterThanOrEqual.class),
            @Spec(path = "version", spec = Equal.class)          
    })
    public interface EntityGroupSpec<T extends AbstractEntity<?,?>> extends Specification<T> {};


    @And({  
        @Spec(path="nome", spec= Like.class)
    })
    public interface ClienteSpec extends EntityGroupSpec<Cliente> {};

    @And({  
        @Spec(path="nome", spec= Like.class)
    })
    public interface PropriedadeSpec extends EntityGroupSpec<Propriedade> {};

    @And({  
        @Spec(path="nome", spec= Like.class)
    })
    public interface LaboratorioSpec extends EntityGroupSpec<Laboratorio> {};


}
