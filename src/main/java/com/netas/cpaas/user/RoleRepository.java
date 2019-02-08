package com.netas.cpaas.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    @Override
    List<Role> findAll();

    Role findByRoleName(Role.RoleName roleName);
}
