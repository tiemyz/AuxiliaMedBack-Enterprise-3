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
import br.com.fiap.AuxiliaMedAPI.models.Consultorio;
import br.com.fiap.AuxiliaMedAPI.repository.ConsultorioRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/consultorio")
@Slf4j
@SecurityRequirement(name = "bearer-key")
@Tag(name = "consultorio")
public class ConsultorioController {
    
    @Autowired
    ConsultorioRepository consultorioRepository; 

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    @GetMapping
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) String busca, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var consultorio = (busca == null) ? 
            consultorioRepository.findAll(pageable): 
            consultorioRepository.findByIdContaining(busca, pageable);

        return assembler.toModel(consultorio.map(Consultorio::toEntityModel)); 
    }

    @PostMapping
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "o consultorio foi cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "os dados enviados são inválidos")
    })
    public ResponseEntity<EntityModel<Consultorio>> create(
            @RequestBody @Valid Consultorio consultorio,
            BindingResult result) {
        log.info("cadastrando informação: " + consultorio);
        consultorioRepository.save(consultorio);
        return ResponseEntity
            .created(consultorio.toEntityModel().getRequiredLink("self").toUri())
            .body(consultorio.toEntityModel());
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Detalhes do consultorio",
        description = "Retornar os dados do consultorio de acordo com o id informado no path"
    )
    public EntityModel<Consultorio> show(@PathVariable Long id) {
        log.info("buscando informação: " + id);
        return getConsultorio(id).toEntityModel();
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Consultorio> destroy(@PathVariable Long id){
        log.info("apagando informação: " + id);
        consultorioRepository.delete(getConsultorio(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Consultorio>> update(
            @PathVariable Long id,
            @RequestBody @Valid Consultorio consultorio) {
        log.info("atualizando informação: " + id);
        getConsultorio(id);
        consultorio.setId(id);
        consultorioRepository.save(consultorio);
        return ResponseEntity.ok(consultorio.toEntityModel());
    }

    private Consultorio getConsultorio(Long id) {
        return consultorioRepository.findById(id)
                .orElseThrow(() -> new RestNotFoundException("informação não encontrada"));
    }
}
