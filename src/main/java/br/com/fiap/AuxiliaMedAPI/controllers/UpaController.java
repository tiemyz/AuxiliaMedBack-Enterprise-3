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
import br.com.fiap.AuxiliaMedAPI.models.Upa;
import br.com.fiap.AuxiliaMedAPI.repository.UpaRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/upa")
@Slf4j
@SecurityRequirement(name = "bearer-key")
@Tag(name = "upa")
public class UpaController {
    
    @Autowired
    UpaRepository upaRepository; 

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    @GetMapping
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) String busca, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var upa = (busca == null) ? 
            upaRepository.findAll(pageable): 
            upaRepository.findByIdContaining(busca, pageable);

        return assembler.toModel(upa.map(Upa::toEntityModel)); 
    }

    @PostMapping
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "a upa foi cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "os dados enviados são inválidos")
    })
    public ResponseEntity<EntityModel<Upa>> create(
            @RequestBody @Valid Upa upa,
            BindingResult result) {
        log.info("cadastrando informação: " + upa);
        upaRepository.save(upa);
        return ResponseEntity
            .created(upa.toEntityModel().getRequiredLink("self").toUri())
            .body(upa.toEntityModel());
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Detalhes da upa",
        description = "Retornar os dados do upa de acordo com o id informado no path"
    )
    public EntityModel<Upa> show(@PathVariable Long id) {
        log.info("buscando informação: " + id);
        return getUpa(id).toEntityModel();
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Upa> destroy(@PathVariable Long id){
        log.info("apagando informação: " + id);
        upaRepository.delete(getUpa(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Upa>> update(
            @PathVariable Long id,
            @RequestBody @Valid Upa upa) {
        log.info("atualizando informação: " + id);
        getUpa(id);
        upa.setId(id);
        upaRepository.save(upa);
        return ResponseEntity.ok(upa.toEntityModel());
    }

    private Upa getUpa(Long id) {
        return upaRepository.findById(id)
                .orElseThrow(() -> new RestNotFoundException("informação não encontrada"));
    }
}