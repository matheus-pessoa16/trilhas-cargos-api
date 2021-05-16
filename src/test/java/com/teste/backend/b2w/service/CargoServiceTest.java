package com.teste.backend.b2w.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.teste.backend.b2w.exception.BusinessException;
import com.teste.backend.b2w.exception.ExceptionCode;
import com.teste.backend.b2w.model.Cargo;
import com.teste.backend.b2w.repository.CargoRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CargoServiceTest {

    @Mock
    private CargoRepository cargoRepository;

    private CargoService cargoService;

    @BeforeEach
    void initCargoService() {
        cargoService = new CargoService(cargoRepository);
    }

    private Cargo createCargo() {
        return Cargo.builder().cargoMissao("Missão").cargoNome("Nome").id(1).trilhaId(1).build();
    }

    @Test
    public void createCargoTest() {
        Cargo cargo = createCargo();
        when(cargoRepository.save(any(Cargo.class))).thenReturn( cargo );
        Cargo cargoSalvo = cargoService.createCargo(cargo);
        assertNotNull(cargoSalvo);
        assertEquals(cargo.getId(), 1);
        assertNotNull(cargo.getDataAtualizacao());
        verify(cargoRepository).save(cargo);
    }

    @Test
    public void shouldNotSaveCargoWithRepeatedName() {
        Cargo cargo = createCargo();

        Cargo cargo2 = createCargo();
        cargo2.setId(2);

        when(cargoRepository.findByCargoNome(cargo.getCargoNome())).thenReturn( cargo2 );

        assertThrows(BusinessException.class, () -> {
            cargoService.createCargo(cargo);
        });
        
        Throwable exception = Assertions.catchThrowable(() -> cargoService.createCargo(cargo));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.CARGO_JA_EXISTE.getMessage());
    }

    @Test
    public void shouldNotCreateCargoWithoutTrilha() {
        Cargo cargo = createCargo();
        cargo.setTrilhaId(null);

        assertThrows(BusinessException.class, () -> {
            cargoService.createCargo(cargo);
        });
        
        Throwable exception = Assertions.catchThrowable(() -> cargoService.createCargo(cargo));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.CARGO_SEM_TRILHA.getMessage());
    }

    @Test
    public void findAllTest() {
        List<Cargo> cargos = Arrays.asList(createCargo());
        when(cargoRepository.findAll()).thenReturn( cargos );
        List<Cargo> cargosRegistrados = cargoService.findAll();
        assertEquals(cargos, cargosRegistrados);
        assertEquals(cargos.size(), cargosRegistrados.size());
        verify(cargoRepository).findAll();
    }

    @Test
    public void findByIdTest() {
        Cargo cargo = createCargo();
        when(cargoRepository.findById(cargo.getId())).thenReturn( Optional.of(cargo) );
        Optional<Cargo> cargoEncontrado = Optional.of(cargoService.findById(cargo.getId()));
        assertTrue(cargoEncontrado.isPresent());
        assertEquals(cargoEncontrado.get().getId(), cargo.getId());
        verify(cargoRepository).findById(cargo.getId());
    }

    @Test
    public void shouldNotFindInvalidId() {
        Cargo cargo = createCargo();
        cargo.setId(null);
        assertThrows(BusinessException.class, () -> {
            cargoService.findById(cargo.getId());
        });        
        Throwable exception = Assertions.catchThrowable(() -> cargoService.findById(cargo.getId()));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.CARGO_NAO_ENCONTRADO.getMessage());
    }

    @Test
    public void shoulNotFindAnyCargoWhenIdDoesNotExist() {
        Cargo cargo = createCargo();
        cargo.setId(82);
        assertThrows(BusinessException.class, () -> {
            cargoService.findById(cargo.getId());
        }); 
        Throwable exception = Assertions.catchThrowable(() -> cargoService.findById(cargo.getId()));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.CARGO_NAO_ENCONTRADO.getMessage());
    }

    @Test
    public void deleteCargoTest() {
        Cargo cargo = createCargo();
        when(cargoRepository.findById(cargo.getId())).thenReturn(Optional.of(cargo));
        cargoService.deleteById(cargo.getId());
        verify(cargoRepository).deleteById(cargo.getId());
    }


    @Test
    public void shouldNotDeleteInvalidId() {
        Cargo cargo = createCargo();
        cargo.setId(82);
        assertThrows(BusinessException.class, () -> {
            cargoService.deleteById(cargo.getId());
        });  
        Throwable exception = Assertions.catchThrowable(() -> cargoService.deleteById(cargo.getId()));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.CARGO_NAO_ENCONTRADO.getMessage());
    }

    @Test
    public void updateCargoTest() {
        Cargo cargo = createCargo();
        when(cargoRepository.findById(cargo.getId())).thenReturn(Optional.of(cargo));
        when(cargoRepository.findByCargoNome(cargo.getCargoNome())).thenReturn( cargo );
        cargoService.updateCargo(cargo.getId(), cargo);
        verify(cargoRepository).findById(cargo.getId());
        verify(cargoRepository).save(cargo);
    }

    @Test
    public void shouldNotUpdateInvalidCargo() {
        Cargo cargo = createCargo();
        Cargo cargo2 = createCargo();
        cargo2.setId(2);
        when(cargoRepository.findById(cargo.getId())).thenReturn(Optional.of(cargo));
        when(cargoRepository.findByCargoNome(cargo.getCargoNome())).thenReturn( cargo2 );
        
        assertThrows(BusinessException.class, () -> {
            cargoService.updateCargo(cargo.getId(), cargo);
        });

        Throwable exception = Assertions.catchThrowable(() -> cargoService.updateCargo(cargo.getId(), cargo));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.CARGO_JA_EXISTE.getMessage());
    }

    @Test
    public void shouldNotUpdateCargoWithInvalidId() {
        Cargo cargo = new Cargo();
        assertThrows(BusinessException.class, () -> {
            cargoService.updateCargo(cargo.getId(), cargo);
        });
        Throwable exception = Assertions.catchThrowable(() -> cargoService.updateCargo(cargo.getId(), cargo));
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(ExceptionCode.CARGO_NAO_ENCONTRADO.getMessage());
    }

}
