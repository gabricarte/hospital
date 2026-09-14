package br.com.fiap.appointment_api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String crm;

    @Column(nullable = false, length = 50)
    private String specialty;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void updateInformation(String name, String specialty) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (specialty != null && !specialty.isBlank()) {
            this.specialty = specialty;
        }
    }
}
