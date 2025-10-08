package com.coderscenter.backend.entities.profile;

import com.coderscenter.backend.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "permission_id"})
    )
    private List<Permission> permissions = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", unique = true)
    private Profile profile;

    @Column(nullable = false)
    private boolean loggedInBefore; // to check whether it is first login or not

    // === Security Flags ===
    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean isLocked;

    @Column(nullable = false)
    private boolean isExpired;

    // === Custom Constructor ===
    public User(String username, String email, String password, Role role, List<Permission> permissions) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.permissions = permissions;
        this.profile = new Profile();
        this.loggedInBefore = false;
        this.enabled = true;
        this.isLocked = false;
        this.isExpired = false;
    }

    // Hier werden die Berechtigungen des Users geladen WICHTIG!!! die Role ist auch eine Berechtigung
    // In diesem Beispiel ist die einzige Berechtigung die ein User hat seine Rolle man könnte es aber auch um eigene Berechtigungen erweitern
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
        permissions.forEach(permission -> authorityList.add(new SimpleGrantedAuthority("ROLE_" + permission.getName().toString())));
        return authorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
