package br.com.fiap.appointment_api.service;

import br.com.fiap.appointment_api.domain.entity.Patient;
import br.com.fiap.appointment_api.domain.entity.User;
import br.com.fiap.appointment_api.domain.enums.UserRole;
import br.com.fiap.appointment_api.exception.BusinessException;
import br.com.fiap.appointment_api.exception.ResourceNotFoundException;
import br.com.fiap.appointment_api.repository.PatientRepository;
import br.com.fiap.appointment_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Patient createPatient(String name, String cpf, String email, String username, String password) {
        validatePatient(cpf, email, username);

        User user = userRepository.save(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .role(UserRole.PATIENT)
                        .build()
        );

        Patient patient = Patient.builder()
                .name(name)
                .cpf(cpf)
                .email(email)
                .user(user)
                .build();

        return patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public Patient findById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado. "));
    }

    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Transactional
    public Patient updatePatient(Long patientId, String name, String email) {
        Patient patient = findById(patientId);

        if (!patient.getEmail().equals(email) && patientRepository.existsByEmail(email)) {
            throw new BusinessException("Um paciente com esse e-mail já foi registrado. ");
        }

        patient.updateInformation(name, email);

        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long patientId) {
        Patient patient = findById(patientId);
        patientRepository.delete(patient);
    }

    private void validatePatient(String cpf, String email, String username) {
        if (patientRepository.existsByCpf(cpf)) {
            throw new BusinessException("Já existe um paciente com esse CPF. ");
        }

        if (patientRepository.existsByEmail(email)) {
            throw new BusinessException("Já existe um paciente com esse e-mail. ");
        }

        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Já existe um paciente com esse username. ");
        }
    }
}