package br.com.fiap.appointment_api.repository;

import br.com.fiap.appointment_api.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
