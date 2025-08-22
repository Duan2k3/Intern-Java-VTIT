package com.example.book_web.controller;

import com.example.book_web.Base.ResponseDto;
import com.example.book_web.common.ResponseConfig;
import com.example.book_web.dto.PermissionDTO;
import com.example.book_web.entity.Permission;
import com.example.book_web.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/permission")
public class PermissionController {
    private final PermissionService permissionService;
    @PostMapping("/create")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CREATE_PERMISSION')")
    public ResponseEntity<?> createPermission(@RequestBody PermissionDTO permissionDTO){
            return ResponseConfig.success(permissionService.createPermission(permissionDTO),"Thanh cong");
    }
    @PutMapping("/update/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERMISSION')")
    public ResponseEntity<ResponseDto<Permission>> updatePermission(@RequestBody PermissionDTO permissionDTO,@PathVariable Long id){

            return ResponseConfig.success(permissionService.updatePermission(id,permissionDTO));
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DELETE_PERMISSION')")
    public ResponseEntity<?> deletePermission(@PathVariable Long id){
        permissionService.deletePermission(id);
      return   ResponseConfig.success(null,"Thanh cong");

    }
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_VIEW_PERMISSION')")
    public  ResponseEntity<ResponseDto<List<PermissionDTO>>> findAllPermission(){

        return ResponseConfig.success(permissionService.getAllPermission(),"Thanh cong");
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_PERMISSION')")
    public  ResponseEntity<ResponseDto<List<String>>> getDetailPermissionUser(@PathVariable Long id){
            return ResponseConfig.success(permissionService.getDetailPermission(id));
        }



}
