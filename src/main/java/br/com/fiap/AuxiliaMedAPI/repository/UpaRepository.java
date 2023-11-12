package br.com.fiap.AuxiliaMedAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.AuxiliaMedAPI.models.Upa;

public interface UpaRepository extends JpaRepository<Upa, Long> {
    Page<Upa> findByIdContaining(String busca, Pageable pageable);
}