package br.com.fiap.AuxiliaMedAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.AuxiliaMedAPI.models.Consultorio;

public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
    Page<Consultorio> findByIdContaining(String busca, Pageable pageable);
}
