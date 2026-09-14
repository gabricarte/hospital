package br.com.fiap.appointment_api.service;

import br.com.fiap.appointment_api.domain.dto.AppointmentNotificationDTO;
import br.com.fiap.appointment_api.domain.entity.Appointment;
import br.com.fiap.appointment_api.domain.entity.Doctor;
import br.com.fiap.appointment_api.domain.entity.Patient;
import br.com.fiap.appointment_api.exception.ResourceNotFoundException;
import br.com.fiap.appointment_api.publisher.AppointmentMessagePublisher;
import br.com.fiap.appointment_api.repository.AppointmentRepository;
import br.com.fiap.appointment_api.repository.DoctorRepository;
import br.com.fiap.appointment_api.repository.PatientRepository;
import br.com.fiap.appointment_api.util.AppointmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentValidator validator;
    private final AppointmentMessagePublisher messagePublisher;
    private final ObjectMapper objectMapper;

    @Transactional
    public Appointment create(Long patientId, Long doctorId, LocalDateTime dateTime) {
        validator.validateCreation(patientId, doctorId, dateTime);

        Patient patient = getPatient(patientId);
        Doctor doctor = getDoctor(doctorId);

        Appointment appointment = new Appointment(patient, doctor, dateTime);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        sendNotification(savedAppointment);

        return savedAppointment;
    }

    @Transactional(readOnly = true)
    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada."));
    }

    public List<Appointment> findByUsername(String username) {
        return appointmentRepository.findByPatientUserUsername(username);
    }

    @Transactional(readOnly = true)
    public List<Appointment> findHistoryByPatientId(Long patientId) {
        validator.validatePatientExists(patientId);
        return appointmentRepository.findByPatientIdAndDateTimeBefore(patientId, LocalDateTime.now());
    }

    @Transactional
    public Appointment update(Long id, Long doctorId, LocalDateTime dateTime) {
        Appointment appointment = findById(id);
        validator.validateUpdate(appointment, doctorId, dateTime);

        Doctor doctor = getDoctor(doctorId);
        appointment.reschedule(doctor, dateTime);

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        sendNotification(updatedAppointment);

        return updatedAppointment;
    }

    @Transactional
    public Appointment cancel(Long id) {
        Appointment appointment = findById(id);
        validator.validateStateTransition(appointment);

        appointment.cancel();

        Appointment cancelledAppointment = appointmentRepository.save(appointment);
        sendNotification(cancelledAppointment);

        return cancelledAppointment;
    }

    @Transactional
    public Appointment complete(Long id) {
        Appointment appointment = findById(id);
        validator.validateStateTransition(appointment);

        appointment.complete();

        Appointment completedAppointment = appointmentRepository.save(appointment);
        sendNotification(completedAppointment);

        return completedAppointment;
    }

    private Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));
    }

    private Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado."));
    }

    private void sendNotification(Appointment appointment) {
        try {
            AppointmentNotificationDTO dto = new AppointmentNotificationDTO(
                    appointment.getPatient().getEmail(),
                    appointment.getPatient().getName(),
                    appointment.getDoctor().getName(),
                    appointment.getDateTime()
            );

            String message = objectMapper.writeValueAsString(dto);
            messagePublisher.sendAppointmentMessage(message);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o envio da notificação.", e);
        }
    }
}