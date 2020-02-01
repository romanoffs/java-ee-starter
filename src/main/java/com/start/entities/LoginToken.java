package com.start.entities;

import lombok.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.MONTHS;
import static javax.persistence.EnumType.STRING;
import static org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL;

@NamedQueries({
        @NamedQuery(name = "LoginToken.findByHashAndType", query = "SELECT t FROM LoginToken t WHERE t.tokenHash = :hash AND t.type = :type"),
        @NamedQuery(name = "LoginToken.removeByHashAndType", query = "DELETE LoginToken t WHERE t.tokenHash = :hash AND t.type = :type"),
        @NamedQuery(name = "LoginToken.removeExpired", query = "DELETE LoginToken t WHERE t.expiration <= :now"),
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"id", "tokenHash"})
@Table(name = "tokens")
@Entity
public class LoginToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int HASH_LENGTH = 64;
    private static final int IP_ADDRESS_MAXLENGTH = 45;
    private static final int DESCRIPTION_MAXLENGTH = 255;

    public enum TokenType {
        REMEMBER_ME,
        API,
        RESET_PASSWORD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = HASH_LENGTH, nullable = false, unique = true)
    private byte[] tokenHash;

    @NotNull
    @Column(nullable = false)
    private Instant created;

    @NotNull
    @Column(nullable = false)
    private Instant expiration;

    @NotNull
    @Size(max = IP_ADDRESS_MAXLENGTH)
    @Column(length = IP_ADDRESS_MAXLENGTH, nullable = false)
    private String ipAddress;

    @Size(max = DESCRIPTION_MAXLENGTH)
    @Column(length = DESCRIPTION_MAXLENGTH)
    private String description;

    @ManyToOne(optional = false)
    @Cache(usage = TRANSACTIONAL)
    private User user;

    @Enumerated(STRING)
    private TokenType type;

    @PrePersist
    public void setTimestamps() {
        created = Instant.now();

        if (expiration == null) {
            expiration = created.plus(1, MONTHS);
        }
    }
}