package com.zidioDev.ExpenseManager.repository;

import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isDeleted = false")
    List<User> findByRole(@Param("role") Role role);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false")
    List<User> findAllActiveUsers(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false")
    Page<User> findAllActivePaged(Pageable pageable);
}

