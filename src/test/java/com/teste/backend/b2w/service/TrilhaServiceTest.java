package com.teste.backend.b2w.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.teste.backend.b2w.exception.BusinessException;
import com.teste.backend.b2w.exception.ExceptionCode;
import com.teste.backend.b2w.model.Cargo;
import com.teste.backend.b2w.model.Trilha;
import com.teste.backend.b2w.repository.TrilhaRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrilhaServiceTest {
    
    @Mock
    private TrilhaRepository trilhaRepository;

    private TrilhaService trilhaService;

    @BeforeEach
    void initCargoService() {
        trilhaService = new TrilhaService(trilhaRepository);
    }

    private Trilha createTrilha() {
        return Trilha.builder()
        .cargos(new ArrayList<Cargo>())
        .diretoria("Diretoria")
        .id(1)
        .missaoAlternativa("Missao Alternativa")
        .missaoFormal("missaoFormal")
        .trilhaNome("Nome da trilha")
        .build();
    }

    @Test
    public void findAllTest() {
        List<Trilha> trilhas = Arrays.asList(createTrilha());
        when(trilhaRepository.findAll()).thenReturn( trilhas );
        List<Trilha> trilhasRegistradas = trilhaService.findAll();
        assertEquals(trilhas, trilhasRegistradas);
        assertEquals(trilhas.size(), trilhasRegistradas.size());
        verify(trilhaRepository).findAll();
    }

    @Test
    public void createTrilhaTest() {
        Trilha trilha = createTrilha();
        when(trilhaRepository.save(any(Trilha.class))).thenReturn(trilha);
        Trilha trilhaSalva = trilhaService.createTrilha(trilha);
        assertNotNull(trilhaSalva);
        assertEquals(trilhaSalva.getId(), 1);
        assertNotNull(trilhaSalva.getDataAtualizacao());
        verify(trilhaRepository).save(trilha);
    }
    
    @Test
    public void shouldNotCreateTrilhaWithRepeatedName() {
        Trilha trilha1 = createTrilha();
        Trilha trilha2 = createTrilha();
        trilha2.setId(2);
        when(trilhaRepository.findByTrilhaNome(trilha1.getTrilhaNome())).thenReturn(trilha2);
        assertThrows(BusinessException.class, () -> {
            trilhaService.createTrilha(trilha1);
        });
        Throwable exception = Assertions.catchThrowable(() -> trilhaService.createTrilha(trilha1));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.TRILHA_NOME_JA_CADASTRADO.getMessage());
    }

    @Test
    public void findByIdTest() {
        Trilha trilha = createTrilha();
        when(trilhaRepository.findById(trilha.getId())).thenReturn(Optional.of(trilha));
        Optional<Trilha> trilhaEncontrada = Optional.of(trilhaService.findById(trilha.getId()));
        Assertions.assertThat(trilhaEncontrada).isNotEmpty();
        Assertions.assertThat(trilhaEncontrada).isNotNull();
        verify(trilhaRepository).findById(trilha.getId());
    }

    @Test
    public void shouldNotFindInvalidTrilhaID() {
        Trilha trilha = new Trilha();
        assertThrows(BusinessException.class, () -> {
            trilhaService.findById(trilha.getId());
        });
        Throwable exception = Assertions.catchThrowable(() -> trilhaService.findById(trilha.getId()));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.TRILHA_NAO_ENCONTRADA.getMessage());
    }

    @Test
    public void shouldNotFindTrilhaWithInexistentID() {
        Trilha trilha = new Trilha();
        trilha.setId(200);
        assertThrows(BusinessException.class, () -> {
            trilhaService.findById(trilha.getId());
        });
        Throwable exception = Assertions.catchThrowable(() -> trilhaService.findById(trilha.getId()));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.TRILHA_NAO_ENCONTRADA.getMessage());
    }

    @Test
    public void updateTrilhaTest() {
        Trilha trilha = createTrilha();
        when(trilhaRepository.findById(trilha.getId())).thenReturn(Optional.of(trilha));
        when(trilhaRepository.save(trilha)).thenReturn(trilha);
        Trilha trilhaAtualizada = trilhaService.updateTrilha(trilha.getId(), trilha);
        
        Assertions.assertThat(trilhaAtualizada.getId()).isEqualTo(trilha.getId());
        Assertions.assertThat(trilhaAtualizada.getCargos()).isEqualTo(trilha.getCargos());
        Assertions.assertThat(trilhaAtualizada.getDiretoria()).isEqualTo(trilha.getDiretoria());
        Assertions.assertThat(trilhaAtualizada.getMissaoAlternativa()).isEqualTo(trilha.getMissaoAlternativa());
        Assertions.assertThat(trilhaAtualizada.getMissaoFormal()).isEqualTo(trilha.getMissaoFormal());
        Assertions.assertThat(trilhaAtualizada.getTrilhaNome()).isEqualTo(trilha.getTrilhaNome());
       
        verify(trilhaRepository).findById(trilha.getId());
        verify(trilhaRepository).save(trilha);
    }

    @Test
    public void sholdNotUpdateTrilhaWithRepeatedName() {

        Trilha trilha1 = createTrilha();
        Trilha trilha2 = createTrilha();
        trilha2.setId(2);

        when(trilhaRepository.findById(trilha1.getId())).thenReturn(Optional.of(trilha1));
        when(trilhaRepository.findByTrilhaNome(trilha1.getTrilhaNome())).thenReturn(trilha2);
        
        assertThrows(BusinessException.class, () -> {
            trilhaService.updateTrilha(trilha1.getId(), trilha1);
        });
        Throwable exception = Assertions.catchThrowable(() -> trilhaService.updateTrilha(trilha1.getId(), trilha1));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.TRILHA_NOME_JA_CADASTRADO.getMessage());
        
    }

    @Test
    public void shouldNotUpdateTrilhaWithInvalidId() {
        Trilha trilha = createTrilha();
        assertThrows(BusinessException.class, () -> {
           trilhaService.updateTrilha(trilha.getId(), trilha);
        });
        Throwable exception = Assertions.catchThrowable(() -> trilhaService.findById(trilha.getId()));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.TRILHA_NAO_ENCONTRADA.getMessage());
        verify(trilhaRepository, never()).save(trilha);
    }


    @Test
    public void deleteByIdTest() {
        Trilha trilha = createTrilha();
        when(trilhaRepository.findById(trilha.getId())).thenReturn(Optional.of(trilha));
        trilhaService.deleteById(trilha.getId());
        verify(trilhaRepository).findById(trilha.getId());
        verify(trilhaRepository).deleteById(trilha.getId());
       
    }

    @Test
    public void shouldNotDeleteInvalidId() {
        Trilha trilha = new Trilha();
        assertThrows(BusinessException.class, () -> {
            trilhaService.deleteById(trilha.getId());
         });
         Throwable exception = Assertions.catchThrowable(() ->trilhaService.deleteById(trilha.getId()));
         Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.TRILHA_NAO_ENCONTRADA.getMessage());
        verify(trilhaRepository, never()).deleteById(trilha.getId());
    }



}
