package com.teste.backend.b2w.repository;

import com.teste.backend.b2w.model.Trilha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrilhaRepository extends JpaRepository<Trilha, Integer> {

    Trilha findByTrilhaNome(String nomeTrilha);
    
}
