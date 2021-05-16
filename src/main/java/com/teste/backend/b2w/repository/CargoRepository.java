package com.teste.backend.b2w.repository;

import com.teste.backend.b2w.model.Cargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    Cargo findByCargoNome(String nomeDoCargo);
    
}
