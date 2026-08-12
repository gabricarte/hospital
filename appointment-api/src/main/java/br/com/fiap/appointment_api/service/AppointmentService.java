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
    public Appointment create(
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

        if (appointmentRepository.existsByPatientIdAndDateTime(
                patientId,
                dateTime
        )) {
            throw new BusinessException(
                    "O paciente já possui uma consulta nesse horário."
            );
        }

        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDateTime(dateTime);
        appointment.setStatus(AppointmentStatus.AGENDADA);

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
    public List<Appointment> findByPatientId(Long patientId) {

        validatePatient(patientId);

        return appointmentRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> findFutureByPatientId(Long patientId) {

        validatePatient(patientId);

        return appointmentRepository.findByPatientIdAndDateTimeAfter(
                patientId,
                LocalDateTime.now()
        );
    }

    @Transactional(readOnly = true)
    public List<Appointment> findHistoryByPatientId(Long patientId) {

        validatePatient(patientId);

        return appointmentRepository.findByPatientIdAndDateTimeBefore(
                patientId,
                LocalDateTime.now()
        );
    }

    @Transactional
    public Appointment cancel(Long id) {

        Appointment appointment = findById(id);

        if (appointment.getStatus() != AppointmentStatus.AGENDADA) {
            throw new BusinessException(
                    "Apenas consultas agendadas podem ser canceladas."
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELADA);

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment complete(Long id) {

        Appointment appointment = findById(id);

        if (appointment.getStatus() != AppointmentStatus.AGENDADA) {
            throw new BusinessException(
                    "Apenas consultas agendadas podem ser concluídas."
            );
        }

        appointment.setStatus(AppointmentStatus.CONCLUIDA);

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