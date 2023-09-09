package br.com.fiap.AuxiliaMedAPI.models;

import org.springframework.hateoas.EntityModel;
import br.com.fiap.AuxiliaMedAPI.controllers.ConsultorioController;
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
@Table(name= "T_AM_CONSULTORIO")
public class Consultorio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(min = 5, max = 200, message = "deve ser o nome real do consultório")
    private String nomeConsultorio;

    @NotBlank @Size(min = 5, max = 4000, message = "deve ser a descrição passo a passo")
    private String ctLogin;

    @NotBlank @Size(min = 5, max = 4000, message = "deve ser a descrição passo a passo")
    private String ctCadastro;

    @NotBlank @Size(min = 5, max = 4000, message = "deve ser a descrição passo a passo")
    private String ctConsulta;

    @NotBlank @Size(min = 5, max = 4000, message = "deve ser a descrição passo a passo")
    private String ctLocalidade;

    public EntityModel<Consultorio> toEntityModel() {
        return EntityModel.of(
            this, 
            linkTo(methodOn(ConsultorioController.class).show(id)).withSelfRel(),
            linkTo(methodOn(ConsultorioController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(ConsultorioController.class).index(null, Pageable.unpaged())).withRel("all")
        );
    }
}
