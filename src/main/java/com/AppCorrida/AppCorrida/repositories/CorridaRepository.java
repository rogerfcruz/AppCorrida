package com.AppCorrida.AppCorrida.repositories;

import com.AppCorrida.AppCorrida.entities.Corrida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorridaRepository extends JpaRepository<Corrida, Long> {
}
