package com.AppCorrida.AppCorrida.services;

import com.AppCorrida.AppCorrida.entities.Motorista;
import com.AppCorrida.AppCorrida.repositories.MotoristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotoristaService {

    @Autowired
    MotoristaRepository motoristaRepository;

    public List<Motorista> findAll(){
        return motoristaRepository.findAll();
    }

    public Motorista findById(Long id){
        Optional<Motorista> motorista = motoristaRepository.findById(id);
        return motorista.get();
    }

    public Motorista create(Motorista motorista){
        return motoristaRepository.save(motorista);
    }

    public Motorista update(Motorista motorista){
        return motoristaRepository.save(motorista);
    }

    public void delete(Long id){
        motoristaRepository.deleteById(id);
    }
}
