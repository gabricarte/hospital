package br.com.fiap.appointment_api.service;

import br.com.fiap.appointment_api.domain.entity.Doctor;
import br.com.fiap.appointment_api.exception.BusinessException;
import br.com.fiap.appointment_api.exception.ResourceNotFoundException;
import br.com.fiap.appointment_api.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Transactional
    public Doctor createDoctor(
            String name,
            String crm,
            String specialty
    ) {

        if (doctorRepository.existsByCrm(crm)) {
            throw new BusinessException(
                    "Já existe um médico cadastrado com este CRM."
            );
        }

        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setCrm(crm);
        doctor.setSpecialty(specialty);

        return doctorRepository.save(doctor);
    }

    @Transactional(readOnly = true)
    public Doctor findById(Long doctorId) {

        return doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Médico não encontrado."
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Transactional
    public Doctor updateDoctor(
            Long doctorId,
            String name,
            String specialty
    ) {

        Doctor doctor = findById(doctorId);

        doctor.setName(name);
        doctor.setSpecialty(specialty);

        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {

        Doctor doctor = findById(doctorId);

        doctorRepository.delete(doctor);
    }
}