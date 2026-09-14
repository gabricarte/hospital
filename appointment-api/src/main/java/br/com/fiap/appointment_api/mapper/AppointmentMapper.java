package br.com.fiap.appointment_api.mapper;

import br.com.fiap.appointment_api.domain.dto.response.AppointmentResponse;
import br.com.fiap.appointment_api.domain.dto.response.DoctorResponse;
import br.com.fiap.appointment_api.domain.dto.response.PatientResponse;
import br.com.fiap.appointment_api.domain.entity.Appointment;
import br.com.fiap.appointment_api.domain.entity.Doctor;
import br.com.fiap.appointment_api.domain.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                toPatientResponse(appointment.getPatient()),
                toDoctorResponse(appointment.getDoctor()),
                appointment.getDateTime(),
                appointment.getStatus()
        );
    }

    private PatientResponse toPatientResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getName(),
                patient.getCpf(),
                patient.getEmail(),
                patient.getUser().getUsername()
        );
    }

    private DoctorResponse toDoctorResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getCrm(),
                doctor.getSpecialty(),
                doctor.getUser().getUsername()
        );
    }
}