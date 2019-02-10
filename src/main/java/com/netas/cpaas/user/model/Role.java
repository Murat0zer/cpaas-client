package com.netas.cpaas.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
public class Role implements GrantedAuthority, Serializable {

    public static final Role ADMIN = new Role(RoleName.ADMIN);
    public static final Role USER = new Role(RoleName.USER);

    @Id
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName roleName;

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    @JsonCreator
    public Role(String value) {
        this.roleName = RoleName.valueOf(value.toUpperCase());
    }

    @Override
    public String getAuthority() {
        return this.roleName.name();
    }

    public enum RoleName {
        ADMIN,
        USER

    }
}
