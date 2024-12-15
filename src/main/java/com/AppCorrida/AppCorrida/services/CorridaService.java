package com.AppCorrida.AppCorrida.services;

import com.AppCorrida.AppCorrida.entities.Corrida;
import com.AppCorrida.AppCorrida.repositories.CorridaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CorridaService {

    @Autowired
    CorridaRepository corridaRepository;

    public List<Corrida> findAll(){
        return corridaRepository.findAll();
    }

    public Corrida findById(Long id){
        Optional<Corrida> corrida = corridaRepository.findById(id);
        return corrida.get();
    }

    public Corrida create(Corrida corrida){
        return corridaRepository.save(corrida);
    }

    public Corrida update(Corrida corrida){
        return corridaRepository.save(corrida);
    }

    public void delete(Long id){
        corridaRepository.deleteById(id);
    }
}
