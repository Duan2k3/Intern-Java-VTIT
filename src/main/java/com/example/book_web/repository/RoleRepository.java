package com.example.book_web.repository;

import com.example.book_web.entity.Role;
import com.example.book_web.response.RoleResponse;
import com.example.book_web.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name) ;

    @Query("SELECT new com.example.book_web.response.RoleResponse(u.name) FROM Role u")
    List<RoleResponse> findAllRoles();


}
