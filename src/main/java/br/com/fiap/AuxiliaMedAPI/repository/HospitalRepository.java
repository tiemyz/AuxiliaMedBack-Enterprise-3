package br.com.fiap.AuxiliaMedAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.AuxiliaMedAPI.models.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Page<Hospital> findByIdContaining(String busca, Pageable pageable);
}
