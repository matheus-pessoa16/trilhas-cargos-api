package com.teste.backend.b2w.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.teste.backend.b2w.exception.BusinessException;
import com.teste.backend.b2w.exception.ExceptionCode;
import com.teste.backend.b2w.model.Trilha;
import com.teste.backend.b2w.repository.TrilhaRepository;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrilhaService {

    private final TrilhaRepository trilhaRepository;

    public List<Trilha> findAll() {
        return trilhaRepository.findAll();
    }

    private void validateTrilha(Trilha trilha) {

        Trilha trilhaComMesmoNome = findTrilhaByNome(trilha.getTrilhaNome());

        if (Objects.nonNull(trilhaComMesmoNome) && !Objects.equals(trilhaComMesmoNome.getId(), trilha.getId())) {
            throw new BusinessException(ExceptionCode.TRILHA_NOME_JA_CADASTRADO.getMessage());
        }
        
    }

    public Trilha createTrilha(Trilha trilha) {
        validateTrilha(trilha);
        trilha.setDataAtualizacao(LocalDateTime.now());
        return trilhaRepository.save(trilha);
    }

    public Trilha findById(Integer id) {
        return trilhaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.TRILHA_NAO_ENCONTRADA.getMessage()));
    }

    public Trilha updateTrilha(Integer id, Trilha trilha) {
        findById(id);
        trilha.setId(id);
        return createTrilha(trilha);
    }

    public void deleteById(Integer id) {
        Trilha trilha = findById(id);
        trilhaRepository.deleteById(id);
    }

    public Trilha findTrilhaByNome(String nomeTrilha) {
        return trilhaRepository.findByTrilhaNome(nomeTrilha);
    }

}
