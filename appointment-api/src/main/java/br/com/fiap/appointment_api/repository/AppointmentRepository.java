package br.com.fiap.appointment_api.repository;

import br.com.fiap.appointment_api.domain.entity.Appointment;
import br.com.fiap.appointment_api.domain.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByPatientIdAndDateTimeAfter(
            Long patientId,
            LocalDateTime dateTime
    );

    List<Appointment> findByPatientIdAndDateTimeBefore(
            Long patientId,
            LocalDateTime dateTime
    );

    boolean existsByDoctorIdAndDateTime(
            Long doctorId,
            LocalDateTime dateTime
    );
}