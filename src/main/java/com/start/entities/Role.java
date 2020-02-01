package com.start.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@Data
@EqualsAndHashCode(of = {"id"})
public class Role implements Serializable {

    private static final long serialVersionUID = 42L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 255)
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @Column(nullable = false, updatable = false, unique = true)
    private String name;
}
