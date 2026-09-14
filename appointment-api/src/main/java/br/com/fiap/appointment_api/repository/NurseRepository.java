package br.com.fiap.appointment_api.repository;

import br.com.fiap.appointment_api.domain.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
    boolean existsByCoren(String coren);

    Optional<Nurse> findByUserUsername(String username);
}
