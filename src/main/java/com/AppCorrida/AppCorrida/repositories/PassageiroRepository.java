package com.AppCorrida.AppCorrida.repositories;

import com.AppCorrida.AppCorrida.entities.Passageiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassageiroRepository extends JpaRepository<Passageiro, Long> {
}
