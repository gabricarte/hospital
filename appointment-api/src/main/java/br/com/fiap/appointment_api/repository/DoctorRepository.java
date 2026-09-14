package br.com.fiap.appointment_api.repository;

import br.com.fiap.appointment_api.domain.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByCrm(String crm);
}
