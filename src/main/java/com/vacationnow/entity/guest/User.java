package com.vacationnow.entity.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;


@Entity
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @DateTimeFormat
    @Column(nullable = false)
    private Date creationDate;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean enabled;

    @Column(name = "account_non_locked", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean accountNonLocked;

    @Column(name = "failed_attempt", nullable = false)
    private int failedAttempt;

    @Column(name = "lock_time", nullable = true)
    private Date lockTime;

    @Column(name = "verification_code", length = 36, nullable = false)
    private String verificationCode;

    {
        enabled = false;
        accountNonLocked = true;
        failedAttempt = 0;
        lockTime = null;
        verificationCode = UUID.randomUUID().toString();
        creationDate = new Timestamp(System.currentTimeMillis());
    }

}
