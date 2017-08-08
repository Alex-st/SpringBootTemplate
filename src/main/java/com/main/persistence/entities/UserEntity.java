package com.main.persistence.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by Oleksandr on 5/5/2016.
 */
@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_LOGIN", unique=true, nullable = false)
    private String login;

    @Column(name = "USER_PASS", nullable = false)
    private String password;

    @Column(name = "USER_ENABLED", nullable = false)
    private Boolean enabled;

}
