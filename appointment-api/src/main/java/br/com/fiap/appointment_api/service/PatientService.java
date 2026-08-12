package br.com.fiap.appointment_api.service;

import br.com.fiap.appointment_api.domain.entity.Patient;
import br.com.fiap.appointment_api.exception.BusinessException;
import br.com.fiap.appointment_api.exception.ResourceNotFoundException;
import br.com.fiap.appointment_api.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public Patient createPatient(
            String name,
            String cpf,
            String email
    ) {

        if (patientRepository.existsByCpf(cpf)) {
            throw new BusinessException(
                    "Já existe um paciente cadastrado com este CPF."
            );
        }

        if (patientRepository.existsByEmail(email)) {
            throw new BusinessException(
                    "Já existe um paciente cadastrado com este e-mail."
            );
        }

        Patient patient = new Patient();
        patient.setName(name);
        patient.setCpf(cpf);
        patient.setEmail(email);

        return patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public Patient findById(Long patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Paciente não encontrado."
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Transactional
    public Patient updatePatient(
            Long patientId,
            String name,
            String email
    ) {

        Patient patient = findById(patientId);

        if (!patient.getEmail().equals(email)
                && patientRepository.existsByEmail(email)) {

            throw new BusinessException(
                    "Já existe um paciente cadastrado com este e-mail."
            );
        }

        patient.setName(name);
        patient.setEmail(email);

        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long patientId) {

        Patient patient = findById(patientId);

        patientRepository.delete(patient);
    }
}