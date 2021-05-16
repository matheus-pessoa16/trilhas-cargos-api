package com.teste.backend.b2w.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionCode {
    
    CARGO_NAO_ENCONTRADO("Nenhum cargo encontrado"), 
    TRILHA_DIRETORIA_INVALIDA("Insira uma diretoria válida"), 
    TRILHA_MISSAO_FORMAL_INVALIDA("Informe a missão da trilha"), 
    TRILHA_NOME_INVALIDO("O nome da trilha é obrigatório"), 
    TRILHA_NAO_ENCONTRADA("Nehuma trilha encontrada"), 
    TRILHA_MISSAO_ALTERNATIVA_INVALIDA("Informe a missão alternativa"), 
    CARGO_JA_EXISTE("Já existe uma cargo de mesmo nome"), 
    TRILHA_NOME_JA_CADASTRADO("Já existe uma trilha com esse nome"), 
    CARGO_SEM_TRILHA("Informe a trilha");

    private String message;
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
