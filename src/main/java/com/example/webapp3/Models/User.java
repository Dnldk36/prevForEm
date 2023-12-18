package com.example.webapp3.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "usr")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private int balance;
    private boolean active;
    private String email;
    private String activationCode;
    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean activeEmailAccount;



    @Column(columnDefinition = "int DEFAULT 0")
    private int testConfirm;

    @Transient
    private String passwordConfirm;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Active> activity = new ArrayList<>();


    public User(Long id, String username, String password, int balance, String passwordConfirm, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.passwordConfirm = passwordConfirm;
        this.roles = roles;
    }

    public User(Long id, String username, String password, String passwordConfirm, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.roles = roles;
    }

    public boolean getIsActiveEmailAccount() {
        return activeEmailAccount;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
