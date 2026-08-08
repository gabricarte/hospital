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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public Appointment createAppointment(
            Long patientId,
            Long doctorId,
            LocalDateTime dateTime
    ) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Paciente não encontrado."
                        )
                );

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Médico não encontrado."
                        )
                );

        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException(
                    "Não é possível agendar uma consulta para uma data passada."
            );
        }

        boolean doctorBusy =
                appointmentRepository.existsByDoctorIdAndDateTimeAndStatus(
                        doctorId,
                        dateTime,
                        AppointmentStatus.SCHEDULED
                );

        if (doctorBusy) {
            throw new BusinessException(
                    "O médico já possui uma consulta agendada para este horário."
            );
        }

        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDateTime(dateTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Consulta não encontrada."
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Appointment> findByPatientId(Long patientId) {

        validatePatientExists(patientId);

        return appointmentRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> findFutureAppointmentsByPatient(
            Long patientId,
            LocalDateTime dateTime
    ) {
        validatePatientExists(patientId);

        return appointmentRepository
                .findByPatientIdAndDateTimeAfter(
                        patientId,
                        dateTime
                );
    }

    @Transactional(readOnly = true)
    public List<Appointment> findAppointmentHistory(Long patientId) {

        validatePatientExists(patientId);

        return appointmentRepository
                .findByPatientIdAndDateTimeBefore(
                        patientId,
                        LocalDateTime.now()
                );
    }

    @Transactional
    public Appointment completeAppointment(Long id) {

        Appointment appointment = findById(id);

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BusinessException(
                    "Apenas consultas agendadas podem ser concluídas."
            );
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment cancelAppointment(Long id) {

        Appointment appointment = findById(id);

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BusinessException(
                    "Apenas consultas agendadas podem ser canceladas."
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELED);

        return appointmentRepository.save(appointment);
    }

    private void validatePatientExists(Long patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Paciente não encontrado."
                        )
                );
    }
}