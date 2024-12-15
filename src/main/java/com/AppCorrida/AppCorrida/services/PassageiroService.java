package com.AppCorrida.AppCorrida.services;

import com.AppCorrida.AppCorrida.entities.Passageiro;
import com.AppCorrida.AppCorrida.repositories.PassageiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassageiroService {

    @Autowired
    PassageiroRepository passageiroRepository;

    @Autowired
    private PasswordService passwordService;

    public List<Passageiro> findAll(){
        return passageiroRepository.findAll();
    }

    public Passageiro findById(Long id){
        Optional<Passageiro> passageiro = passageiroRepository.findById(id);
        return passageiro.get();
    }

    public Passageiro create(Passageiro passageiro){
        String hashedPassword = passwordService.hashPassword(passageiro.getSenha());
        passageiro.setSenha(hashedPassword);
        return passageiroRepository.save(passageiro);
    }

    public Passageiro update(Passageiro passageiro){
        return passageiroRepository.save(passageiro);
    }

    public void delete(Long id){
        passageiroRepository.deleteById(id);
    }
}
