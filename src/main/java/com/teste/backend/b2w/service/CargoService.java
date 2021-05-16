package com.teste.backend.b2w.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.teste.backend.b2w.exception.BusinessException;
import com.teste.backend.b2w.exception.ExceptionCode;
import com.teste.backend.b2w.model.Cargo;
import com.teste.backend.b2w.repository.CargoRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CargoService {
    
    private final CargoRepository cargoRepository;

    private void validateCargo(Cargo cargo) {
        Cargo cargoComMesmoNome = findByCargoNome(cargo.getCargoNome());
        
        if(!Objects.isNull(cargoComMesmoNome) && !Objects.equals(cargo.getId(), cargoComMesmoNome.getId())) {
            throw new BusinessException(ExceptionCode.CARGO_JA_EXISTE.getMessage());
        }

        if(cargo.getTrilhaId() == null) {
            throw new BusinessException(ExceptionCode.CARGO_SEM_TRILHA.getMessage());
        }
    }

    public Cargo createCargo(Cargo cargo) {
        validateCargo(cargo);
        cargo.setDataAtualizacao(LocalDateTime.now());
        return cargoRepository.save(cargo);
    }

    public List<Cargo> findAll() {
        return cargoRepository.findAll();
    } 

    public Cargo findById(Integer id) {
        return cargoRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionCode.CARGO_NAO_ENCONTRADO.getMessage()));
    }

    public void deleteById(Integer cargoId) {
        Cargo cargoParaDeletar = findById(cargoId);
        cargoRepository.deleteById(cargoParaDeletar.getId());
    }

    public Cargo updateCargo(Integer id, Cargo cargo) {
        cargoRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionCode.CARGO_NAO_ENCONTRADO.getMessage()));
        cargo.setId(id);
        return createCargo(cargo);
    }

    public Cargo findByCargoNome(String nomeDoCargo) {
        return cargoRepository.findByCargoNome(nomeDoCargo);
    }

}
