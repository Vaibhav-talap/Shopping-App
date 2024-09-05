package com.psl.user.service.Repository;

import com.psl.user.service.Entity.Role;
import com.psl.user.service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRoleName(String roleName);

}
