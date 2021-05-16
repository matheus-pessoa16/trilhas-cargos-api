package com.teste.backend.b2w.controller;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.backend.b2w.model.Cargo;
import com.teste.backend.b2w.model.Trilha;
import com.teste.backend.b2w.service.TrilhaService;

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
@WebMvcTest(controllers = TrilhaController.class)
@AutoConfigureMockMvc
public class TrilhaControllerTest {

    static String TRILHA_API = "/trilha";

    @Autowired
    MockMvc mvc;

    @MockBean
    TrilhaService trilhaService;

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
    public void createNovaTrilha() throws Exception {
        Trilha trilha = createTrilha();

        BDDMockito.given(trilhaService.createTrilha(any(Trilha.class))).willReturn(trilha);

        String json = new ObjectMapper().writeValueAsString(trilha);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(TRILHA_API)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("missaoAlternativa").value(trilha.getMissaoAlternativa()))
        .andExpect(MockMvcResultMatchers.jsonPath("missaoFormal").value(trilha.getMissaoFormal()))
        .andExpect(MockMvcResultMatchers.jsonPath("trilhaNome").value(trilha.getTrilhaNome()))
        .andExpect(MockMvcResultMatchers.jsonPath("diretoria").value(trilha.getDiretoria()));
    }
    

    @Test
    public void shouldNotCreateInvalidTrilha() throws Exception {
        Trilha trilha = new Trilha();

        BDDMockito.given(trilhaService.createTrilha(any(Trilha.class))).willReturn(trilha);
        String json = new ObjectMapper().writeValueAsString(trilha);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(TRILHA_API)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(4)));
    }

    @Test
    public void findTrilhaByIdTest() throws Exception {

        Trilha trilha = createTrilha();

        BDDMockito.given(trilhaService.findById(Mockito.anyInt())).willReturn(trilha);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(TRILHA_API.concat("/"+trilha.getId()))
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("missaoAlternativa").value(trilha.getMissaoAlternativa()))
        .andExpect(MockMvcResultMatchers.jsonPath("missaoFormal").value(trilha.getMissaoFormal()))
        .andExpect(MockMvcResultMatchers.jsonPath("trilhaNome").value(trilha.getTrilhaNome()))
        .andExpect(MockMvcResultMatchers.jsonPath("diretoria").value(trilha.getDiretoria()));
    }

    @Test
    public void deleteTrilhaById() throws Exception {
        Trilha trilha = createTrilha();

        BDDMockito.given(trilhaService.findById(Mockito.anyInt())).willReturn(trilha);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(TRILHA_API.concat("/" + trilha.getId()))
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void updateTrilhaTest() throws Exception {
        Trilha trilha = createTrilha();

        Trilha updatedTrilha = Trilha.builder()
        .cargos(new ArrayList<Cargo>())
        .diretoria("Nova Diretoria")
        .id(1)
        .missaoAlternativa("Nova Missao Alternativa")
        .missaoFormal("Nova missão formal")
        .trilhaNome("Outro nome")
        .build();

        BDDMockito.given(trilhaService.findById(trilha.getId())).willReturn(trilha);
        BDDMockito.given(trilhaService.findTrilhaByNome(trilha.getTrilhaNome())).willReturn(trilha);
        BDDMockito.given(trilhaService.updateTrilha(trilha.getId(), trilha)).willReturn(updatedTrilha);

        String json = new ObjectMapper().writeValueAsString(trilha);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(TRILHA_API.concat("/"+trilha.getId()))
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("missaoAlternativa").value(updatedTrilha.getMissaoAlternativa()))
        .andExpect(MockMvcResultMatchers.jsonPath("missaoFormal").value(updatedTrilha.getMissaoFormal()))
        .andExpect(MockMvcResultMatchers.jsonPath("trilhaNome").value(updatedTrilha.getTrilhaNome()))
        .andExpect(MockMvcResultMatchers.jsonPath("diretoria").value(updatedTrilha.getDiretoria()));

    }

    @Test
    public void shouldNotUpdateInvalidCargoTest() throws Exception {
        Trilha trilha = new Trilha();
        BDDMockito.given(trilhaService.findById(Mockito.anyInt())).willReturn(trilha);
        String json = new ObjectMapper().writeValueAsString(trilha);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(TRILHA_API.concat("/" + trilha.getId())).content(json)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
 
}
