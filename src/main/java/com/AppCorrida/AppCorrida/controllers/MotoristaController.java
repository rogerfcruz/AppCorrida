package com.AppCorrida.AppCorrida.controllers;

import com.AppCorrida.AppCorrida.entities.Motorista;
import com.AppCorrida.AppCorrida.services.MotoristaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/motorista")
public class MotoristaController {

    @Autowired
    MotoristaService motoristaService;

    @GetMapping
    public ResponseEntity<List<Motorista>> findAll() {
        List<Motorista> list = motoristaService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Motorista> findById(@PathVariable Long id) {
        Motorista motorista = motoristaService.findById(id);

        if (motorista == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(motorista);
    }

    @PostMapping
    public ResponseEntity<Motorista> create(@RequestBody Motorista motorista){
        motorista = motoristaService.create(motorista);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(motorista.getId()).toUri();
        return ResponseEntity.created(uri).body(motorista);
    }

    @PutMapping
    public ResponseEntity<Motorista> update(@RequestBody Motorista motorista){
        motorista = motoristaService.update(motorista);
        return ResponseEntity.ok().body(motorista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        motoristaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}