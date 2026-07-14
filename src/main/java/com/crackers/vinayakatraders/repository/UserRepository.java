package com.crackers.vinayakatraders.repository;

import com.crackers.vinayakatraders.dto.LoginUserDetailsProjection;
import com.crackers.vinayakatraders.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            SELECT new com.crackers.vinayakatraders.dto.LoginUserDetailsProjection(
                u.email,
                u.password,
                r.id,
                r.name,
                rt.id,
                rt.route,
                rt.heading
            )
            FROM User u
            JOIN Role r ON u.roleId = r.id
            JOIN Routes rt ON rt.role = r.id
            WHERE u.email = :email
        """)
    List<LoginUserDetailsProjection> findLoginDetailsByEmail(@Param("email") String email);
}
