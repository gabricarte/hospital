package br.com.fiap.appointment_api.repository;

import br.com.fiap.appointment_api.domain.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdAndDateTimeBefore(
            Long patientId,
            LocalDateTime dateTime
    );

    boolean existsByDoctorIdAndDateTime(
            Long doctorId,
            LocalDateTime dateTime
    );

    boolean existsByPatientIdAndDateTime(
            Long patientId,
            LocalDateTime dateTime
    );

    List<Appointment> findByPatientUserUsername(String username);
}