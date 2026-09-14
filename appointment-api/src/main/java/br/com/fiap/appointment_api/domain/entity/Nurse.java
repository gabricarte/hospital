package br.com.fiap.appointment_api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nurses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nurse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String coren;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void updateInformation(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }
}