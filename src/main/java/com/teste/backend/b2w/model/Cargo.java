package com.teste.backend.b2w.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cargo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @NotBlank(message = "Informe um nome")
    @Column(name = "cargo_nome")
    @JsonProperty("cargoNome")
    private String cargoNome;

    @Column(name = "trilha_id")
    @JsonProperty("trilhaId")
    private Integer trilhaId;

    @NotBlank(message = "Informe uma missão")
    @Column(name = "cargo_missao")
    @JsonProperty("cargoMissao")
    private String cargoMissao;

    @CreationTimestamp
    @Column(name = "data_atualizacao")
    @JsonProperty("dataAtualizacao")
    private LocalDateTime dataAtualizacao;
}
