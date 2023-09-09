package br.com.fiap.AuxiliaMedAPI.models;

import org.springframework.hateoas.EntityModel;
import br.com.fiap.AuxiliaMedAPI.controllers.HospitalController;
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
@Table(name = "T_AM_HOSPITAL")
public class Hospital {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(min = 5, max = 200, message = "deve ser o nome real do hospital")
    private String nomeHospital;

    @NotBlank @Size(min = 5, max = 4000, message = "deve ser a descrição passo a passo")
    private String hpLogin;

    @NotBlank @Size(min = 5, max = 4000, message = "deve ser a descrição passo a passo")
    private String hpCadastro;

    @NotBlank @Size(min = 5, max = 4000, message = "deve ser a descrição passo a passo")
    private String hpConsulta;

    @NotBlank @Size(min = 5, max = 4000, message = "deve ser a descrição passo a passo")
    private String hpLocalidade;

    
    public EntityModel<Hospital> toEntityModel() {
        return EntityModel.of(
            this, 
            linkTo(methodOn(HospitalController.class).show(id)).withSelfRel(),
            linkTo(methodOn(HospitalController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(HospitalController.class).index(null, Pageable.unpaged())).withRel("all")
        );
    }
}
