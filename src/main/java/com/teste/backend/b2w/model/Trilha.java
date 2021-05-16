package com.teste.backend.b2w.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teste.backend.b2w.exception.ExceptionCode;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trilha")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trilha {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "trilha_nome")
    @NotBlank(message = "Informe o nome da trilha")
    @JsonProperty("trilhaNome")
    private String trilhaNome;

    @Column(name = "diretoria")
    @NotBlank(message = "Informe a diretoria")
    @JsonProperty("diretoria")
    private String diretoria;

    @Column(name = "missao_formal")
    @NotBlank(message = "Insira uma missão")
    @JsonProperty("missaoFormal")
    private String missaoFormal;

    @Column(name = "missao_alternativa")
    @JsonProperty("missaoAlternativa")
    @NotBlank(message = "Insira uma missão alternativa")
    private String missaoAlternativa;

    @CreationTimestamp
    @Column(name = "data_atualizacao")
    @JsonProperty("dateAtualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToMany
    @JoinColumn(name = "trilha_id")
    @JsonProperty("cargos")
    private List<Cargo> cargos = new ArrayList<Cargo>();
}
