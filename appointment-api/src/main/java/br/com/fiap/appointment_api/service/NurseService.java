package br.com.fiap.appointment_api.service;
import br.com.fiap.appointment_api.domain.entity.Nurse;
import br.com.fiap.appointment_api.domain.entity.User;
import br.com.fiap.appointment_api.domain.enums.UserRole;
import br.com.fiap.appointment_api.exception.BusinessException;
import br.com.fiap.appointment_api.exception.ResourceNotFoundException;
import br.com.fiap.appointment_api.repository.NurseRepository;
import br.com.fiap.appointment_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NurseService {

    private final NurseRepository nurseRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Nurse createNurse(String name, String coren, String username, String password) {
        validateNurse(coren, username);

        User user = userRepository.save(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .role(UserRole.NURSE)
                        .build()
        );

        Nurse nurse = Nurse.builder()
                .name(name)
                .coren(coren)
                .user(user)
                .build();

        return nurseRepository.save(nurse);
    }

    @Transactional(readOnly = true)
    public Nurse findById(Long nurseId) {
        return nurseRepository.findById(nurseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enfermeiro(a) não encontrado(a)."));
    }

    @Transactional(readOnly = true)
    public List<Nurse> findAll() {
        return nurseRepository.findAll();
    }

    @Transactional
    public Nurse updateNurse(Long nurseId, String name) {
        Nurse nurse = findById(nurseId);
        nurse.updateInformation(name);
        return nurseRepository.save(nurse);
    }

    @Transactional
    public void deleteNurse(Long nurseId) {
        Nurse nurse = findById(nurseId);
        nurseRepository.delete(nurse);
    }

    private void validateNurse(String coren, String username) {
        if (nurseRepository.existsByCoren(coren)) {
            throw new BusinessException("Já existe um enfermeiro(a) com este COREN.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Já existe um usuário com este username.");
        }
    }
}