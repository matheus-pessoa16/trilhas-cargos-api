package com.teste.backend.b2w.controller;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.backend.b2w.model.Cargo;
import com.teste.backend.b2w.service.CargoService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CargoController.class)
@AutoConfigureMockMvc
public class CargoControllerTest {

    static String CARGO_API = "/cargo";

    @Autowired
    MockMvc mvc;

    @MockBean
    CargoService cargoService;

    private Cargo createCargo() {
        return Cargo.builder().cargoMissao("Missão").cargoNome("Nome").id(1).trilhaId(1).build();
    }

    @Test
    public void createNovoCargo() throws Exception {
        Cargo cargo = createCargo();

        BDDMockito.given(cargoService.createCargo(any(Cargo.class))).willReturn(cargo);

        String json = new ObjectMapper().writeValueAsString(cargo);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(CARGO_API)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("cargoMissao").value(cargo.getCargoMissao()))
        .andExpect(MockMvcResultMatchers.jsonPath("cargoNome").value(cargo.getCargoNome()))
        .andExpect(MockMvcResultMatchers.jsonPath("trilhaId").value(cargo.getTrilhaId()));
    }

    @Test
    public void shouldNotCreateInvalidCargo() throws Exception {
        Cargo cargo = new Cargo();
        String json = new ObjectMapper().writeValueAsString(cargo);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(CARGO_API)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void findCargoByIdTest() throws Exception {
        Cargo cargo = createCargo();

        BDDMockito.given(cargoService.findById(cargo.getId())).willReturn(cargo);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(CARGO_API.concat("/" + cargo.getId()))
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("cargoMissao").value(cargo.getCargoMissao()))
        .andExpect(MockMvcResultMatchers.jsonPath("cargoNome").value(cargo.getCargoNome()))
        .andExpect(MockMvcResultMatchers.jsonPath("trilhaId").value(cargo.getTrilhaId()));
    }

    @Test
    public void deleteCargoByIdTest() throws Exception {
        Cargo cargo = createCargo();
        BDDMockito.given(cargoService.findById(Mockito.anyInt())).willReturn(cargo);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(CARGO_API.concat("/" + cargo.getId()))
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void updateCargoTest() throws Exception {
        Cargo cargo = createCargo();

        Cargo updatedCargo = Cargo.builder().cargoMissao("Nova missão").cargoNome("Novo cargo").id(1).trilhaId(1).build(); 

        BDDMockito.given(cargoService.findById(cargo.getId())).willReturn(cargo);
        BDDMockito.given(cargoService.updateCargo(cargo.getId(), cargo)).willReturn(updatedCargo);

        String json = new ObjectMapper().writeValueAsString(cargo);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(CARGO_API.concat("/" + cargo.getId())).content(json)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("cargoMissao").value(updatedCargo.getCargoMissao()))
        .andExpect(MockMvcResultMatchers.jsonPath("cargoNome").value(updatedCargo.getCargoNome()))
        .andExpect(MockMvcResultMatchers.jsonPath("trilhaId").value(updatedCargo.getTrilhaId()));
    }

    @Test
    public void shouldNotUpdateInvalidCargoTest() throws Exception {
        Cargo cargo = new Cargo();

        BDDMockito.given(cargoService.findById(Mockito.anyInt())).willReturn(cargo);
        String json = new ObjectMapper().writeValueAsString(cargo);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(CARGO_API.concat("/" + cargo.getId())).content(json)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
