package br.com.fiap.AuxiliaMedAPI.models;

import org.springframework.hateoas.EntityModel;
import br.com.fiap.AuxiliaMedAPI.controllers.DoencaController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.data.domain.Pageable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_AM_DOENCA")
public class Doenca {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(min = 5, max = 200, message = "deve ser o nome real da doença")
    private String nomeDoenca;

    @NotBlank @Size(min = 5, max = 4000, message = "deve ser a descrição completa")
    private String dsDoenca;

    public EntityModel<Doenca> toEntityModel() {
        return EntityModel.of(
            this, 
            linkTo(methodOn(DoencaController.class).show(id)).withSelfRel(),
            linkTo(methodOn(DoencaController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(DoencaController.class).index(null, Pageable.unpaged())).withRel("all")
        );
    }

}
