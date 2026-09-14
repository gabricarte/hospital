package br.com.fiap.appointment_api.service;

import br.com.fiap.appointment_api.domain.entity.Doctor;
import br.com.fiap.appointment_api.domain.entity.User;
import br.com.fiap.appointment_api.domain.enums.UserRole;
import br.com.fiap.appointment_api.exception.BusinessException;
import br.com.fiap.appointment_api.exception.ResourceNotFoundException;
import br.com.fiap.appointment_api.repository.DoctorRepository;
import br.com.fiap.appointment_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Doctor createDoctor(String name, String crm, String specialty, String username, String password) {
        validateDoctor(crm, username);

        User user = userRepository.save(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .role(UserRole.DOCTOR)
                        .build()
        );

        Doctor doctor = Doctor.builder()
                .name(name)
                .crm(crm)
                .specialty(specialty)
                .user(user)
                .build();

        return doctorRepository.save(doctor);
    }

    @Transactional(readOnly = true)
    public Doctor findById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Médico(a) não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Transactional
    public Doctor updateDoctor(Long doctorId, String name, String specialty) {
        Doctor doctor = findById(doctorId);

        doctor.updateInformation(name, specialty);

        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = findById(doctorId);
        doctorRepository.delete(doctor);
    }

    private void validateDoctor(String crm, String username) {
        if (doctorRepository.existsByCrm(crm)) {
            throw new BusinessException("Já existe um médico(a) com esse CRM");
        }

        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Já existe um usuário com esse username.");
        }
    }
}