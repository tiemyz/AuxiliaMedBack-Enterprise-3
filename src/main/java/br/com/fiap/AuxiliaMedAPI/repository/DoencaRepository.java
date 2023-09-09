package br.com.fiap.AuxiliaMedAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.AuxiliaMedAPI.models.Doenca;

public interface DoencaRepository extends JpaRepository<Doenca, Long> {
    Page<Doenca> findByIdContaining(String busca, Pageable pageable);
}
