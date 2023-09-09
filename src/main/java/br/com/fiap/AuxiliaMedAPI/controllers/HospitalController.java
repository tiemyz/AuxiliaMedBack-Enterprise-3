package br.com.fiap.AuxiliaMedAPI.controllers;

import org.springdoc.core.annotations.ParameterObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.AuxiliaMedAPI.exception.RestNotFoundException;
import br.com.fiap.AuxiliaMedAPI.models.Hospital;
import br.com.fiap.AuxiliaMedAPI.repository.HospitalRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/hospital")
@Slf4j
@SecurityRequirement(name = "bearer-key")
@Tag(name = "hospital")
public class HospitalController {

    @Autowired
    HospitalRepository hospitalRepository; 

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    @GetMapping
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) String busca, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var hospital = (busca == null) ? 
            hospitalRepository.findAll(pageable): 
            hospitalRepository.findByIdContaining(busca, pageable);

        return assembler.toModel(hospital.map(Hospital::toEntityModel)); 
    }

    @PostMapping
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "o hospital foi cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "os dados enviados são inválidos")
    })
    public ResponseEntity<EntityModel<Hospital>> create(
            @RequestBody @Valid Hospital hospital,
            BindingResult result) {
        log.info("cadastrando informação: " + hospital);
        hospitalRepository.save(hospital);
        return ResponseEntity
            .created(hospital.toEntityModel().getRequiredLink("self").toUri())
            .body(hospital.toEntityModel());
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Detalhes do hospital",
        description = "Retornar os dados do hospital de acordo com o id informado no path"
    )
    public EntityModel<Hospital> show(@PathVariable Long id) {
        log.info("buscando informação: " + id);
        return getHospital(id).toEntityModel();
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Hospital> destroy(@PathVariable Long id){
        log.info("apagando informação: " + id);
        hospitalRepository.delete(getHospital(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Hospital>> update(
            @PathVariable Long id,
            @RequestBody @Valid Hospital hospital) {
        log.info("atualizando informação: " + id);
        getHospital(id);
        hospital.setId(id);
        hospitalRepository.save(hospital);
        return ResponseEntity.ok(hospital.toEntityModel());
    }

    private Hospital getHospital(Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new RestNotFoundException("informação não encontrada"));
    }
}