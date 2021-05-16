package com.teste.backend.b2w.controller;

import java.util.List;

import javax.validation.Valid;

import com.teste.backend.b2w.model.Cargo;
import com.teste.backend.b2w.service.CargoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/cargo")
@RequiredArgsConstructor
public class CargoController {
    
    private final CargoService cargoService;

    @GetMapping
    public List<Cargo> findAll() {
        return cargoService.findAll();
    }

    @PostMapping
    public ResponseEntity<Cargo> createCargo(@RequestBody @Valid Cargo cargo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoService.createCargo(cargo));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Cargo> findCargoById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(cargoService.findById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id) {
        cargoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Removido com sucesso!");
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cargo updateCargo(@RequestBody @Valid Cargo cargo, @PathVariable("id") Integer id) {
        return cargoService.updateCargo(id, cargo);
    }
}
