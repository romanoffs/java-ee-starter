package com.start.entities;

import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = "User.findByUserName", query = "SELECT u FROM User u WHERE u.name = :name"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
})
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User implements Serializable, Principal {
    private static final long serialVersionUID = 42L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false, updatable = false)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    private String email;

    @ToString.Exclude
    @JsonbTransient
    @NotBlank
    @Size(min = 6, max = 255)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    private String password;

    @NotBlank
    @Size(min = 2, max = 255)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    private String name;

    @JsonbDateFormat("DD-MM-YYY HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @JsonbDateFormat("DD-MM-YYY HH:mm:ss")
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    @PreUpdate
    private void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    @PrePersist
    private void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @ToString.Exclude
    @ManyToMany(
            targetEntity = Role.class,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles = new HashSet<>();

}
