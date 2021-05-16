package com.teste.backend.b2w.controller;

import java.util.List;

import javax.validation.Valid;

import com.teste.backend.b2w.model.Trilha;
import com.teste.backend.b2w.service.TrilhaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/trilha")
@RequiredArgsConstructor
public class TrilhaController {
    
    private final TrilhaService trilhaService;

    @GetMapping
    public List<Trilha> findAll() {
        return trilhaService.findAll();
    }

    @PostMapping
    public ResponseEntity<Trilha> createTrilha(@RequestBody @Valid Trilha trilha) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trilhaService.createTrilha(trilha));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Trilha> findTrilhaById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(trilhaService.findById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id) {
        trilhaService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Removido com sucesso!");
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Trilha> updateTrilha(@RequestBody @Valid Trilha trilha, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(trilhaService.updateTrilha(id, trilha));
    }
}
