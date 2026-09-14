package br.com.fiap.appointment_api.util;

import br.com.fiap.appointment_api.domain.entity.Appointment;
import br.com.fiap.appointment_api.domain.enums.AppointmentStatus;
import br.com.fiap.appointment_api.exception.BusinessException;
import br.com.fiap.appointment_api.exception.ResourceNotFoundException;
import br.com.fiap.appointment_api.repository.AppointmentRepository;
import br.com.fiap.appointment_api.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AppointmentValidator {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public void validateCreation(Long patientId, Long doctorId, LocalDateTime dateTime) {
        validatePastDate(dateTime);
        validateDoctorAvailability(doctorId, dateTime);
        validatePatientAvailability(patientId, dateTime);
    }

    public void validateUpdate(Appointment appointment, Long doctorId, LocalDateTime dateTime) {
        validateScheduledStatus(appointment);
        validatePastDate(dateTime);

        if (hasScheduleConflict(appointment, doctorId, dateTime)) {
            validateDoctorAvailability(doctorId, dateTime);
            validatePatientAvailability(appointment.getPatient().getId(), dateTime);
        }
    }

    public void validateStateTransition(Appointment appointment) {
        validateScheduledStatus(appointment);
    }

    public void validatePatientExists(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Paciente não encontrado.");
        }
    }

    private void validatePastDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível agendar uma consulta no passado.");
        }
    }

    private void validateDoctorAvailability(Long doctorId, LocalDateTime dateTime) {
        if (appointmentRepository.existsByDoctorIdAndDateTime(doctorId, dateTime)) {
            throw new BusinessException("O médico já possui uma consulta nesse horário.");
        }
    }

    private void validatePatientAvailability(Long patientId, LocalDateTime dateTime) {
        if (appointmentRepository.existsByPatientIdAndDateTime(patientId, dateTime)) {
            throw new BusinessException("O paciente já possui uma consulta nesse horário.");
        }
    }

    private void validateScheduledStatus(Appointment appointment) {
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BusinessException("Apenas consultas agendadas podem ser modificadas ou canceladas.");
        }
    }

    private boolean hasScheduleConflict(Appointment appointment, Long newDoctorId, LocalDateTime newDateTime) {
        return !appointment.getDateTime().equals(newDateTime) || !appointment.getDoctor().getId().equals(newDoctorId);
    }
}