package com.netas.cpaas.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String password;

    @Embedded
    private ServiceAddress serviceAddress;

    @Embedded
    private UserAddress userAddress;

    @Column(unique = true)
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private String token;

    @OneToOne(cascade = CascadeType.ALL)
    private NvsUser nvsUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
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
        return true;
    }
}
