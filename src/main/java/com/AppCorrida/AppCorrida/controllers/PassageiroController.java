package com.AppCorrida.AppCorrida.controllers;

import com.AppCorrida.AppCorrida.entities.Passageiro;
import com.AppCorrida.AppCorrida.services.PassageiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/passageiro")
public class PassageiroController {

    @Autowired
    PassageiroService passageiroService;

    @GetMapping
    public ResponseEntity<List<Passageiro>> findAll() {
        List<Passageiro> list = passageiroService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passageiro> findById(@PathVariable Long id) {
        Passageiro passageiro = passageiroService.findById(id);

        if (passageiro == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(passageiro);
    }

    @PostMapping
    public ResponseEntity<Passageiro> create(@RequestBody Passageiro passageiro){
        passageiro = passageiroService.create(passageiro);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(passageiro.getId()).toUri();
        return ResponseEntity.created(uri).body(passageiro);
    }

    @PutMapping
    public ResponseEntity<Passageiro> update(@RequestBody Passageiro passageiro){
        passageiro = passageiroService.update(passageiro);
        return ResponseEntity.ok().body(passageiro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        passageiroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}