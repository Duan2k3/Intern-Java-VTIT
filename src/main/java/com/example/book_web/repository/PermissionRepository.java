package com.example.book_web.repository;

import com.example.book_web.dto.PermissionDTO;
import com.example.book_web.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    @Query("SELECT new com.example.book_web.dto.PermissionDTO(u.name,u.description) FROM Permission u")
    List<PermissionDTO> getAllPermission();

    Optional<Permission> findByName(String permission);


}
