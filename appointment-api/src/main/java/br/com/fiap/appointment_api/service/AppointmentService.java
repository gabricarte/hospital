package br.com.fiap.appointment_api.service;

import br.com.fiap.appointment_api.domain.entity.Appointment;
import br.com.fiap.appointment_api.domain.entity.Doctor;
import br.com.fiap.appointment_api.domain.entity.Patient;
import br.com.fiap.appointment_api.domain.enums.AppointmentStatus;
import br.com.fiap.appointment_api.exception.BusinessException;
import br.com.fiap.appointment_api.exception.ResourceNotFoundException;
import br.com.fiap.appointment_api.repository.AppointmentRepository;
import br.com.fiap.appointment_api.repository.DoctorRepository;
import br.com.fiap.appointment_api.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public Appointment create(
            Long patientId,
            Long doctorId,
            LocalDateTime dateTime
    ) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Paciente não encontrado.")
                );

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Médico não encontrado.")
                );

        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException(
                    "Não é possível agendar uma consulta no passado."
            );
        }

        if (appointmentRepository.existsByDoctorIdAndDateTime(
                doctorId,
                dateTime
        )) {
            throw new BusinessException(
                    "O médico já possui uma consulta nesse horário."
            );
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDateTime(dateTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Consulta não encontrada."
                        )
                );
    }

    public List<Appointment> findByPatientId(Long patientId) {
        validatePatient(patientId);

        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> findFutureByPatientId(Long patientId) {
        validatePatient(patientId);

        return appointmentRepository.findByPatientIdAndDateTimeAfter(
                patientId,
                LocalDateTime.now()
        );
    }

    public List<Appointment> findHistoryByPatientId(Long patientId) {
        validatePatient(patientId);

        return appointmentRepository.findByPatientIdAndDateTimeBefore(
                patientId,
                LocalDateTime.now()
        );
    }

    public Appointment cancel(Long id) {
        Appointment appointment = findById(id);

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BusinessException(
                    "Apenas consultas agendadas podem ser canceladas."
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELED);

        return appointmentRepository.save(appointment);
    }

    public Appointment complete(Long id) {
        Appointment appointment = findById(id);

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BusinessException(
                    "Apenas consultas agendadas podem ser concluídas."
            );
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        return appointmentRepository.save(appointment);
    }

    private void validatePatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Paciente não encontrado."
            );
        }
    }
}