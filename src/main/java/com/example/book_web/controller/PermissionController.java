package com.example.book_web.controller;

import com.example.book_web.Base.ResponseDto;
import com.example.book_web.common.ResponseConfig;
import com.example.book_web.components.LocalizationUtils;
import com.example.book_web.dto.PermissionDTO;
import com.example.book_web.entity.Permission;
import com.example.book_web.response.BaseResponse;
import com.example.book_web.service.PermissionService;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/permission")
public class PermissionController {
    private final PermissionService permissionService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/create")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CREATE_PERMISSION')")
    public ResponseEntity<?> createPermission(@RequestBody PermissionDTO permissionDTO){
            return ResponseConfig.success(permissionService.createPermission(permissionDTO),"Thanh cong");
    }
    @PutMapping("/update")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERMISSION')")
    public ResponseEntity<ResponseDto<Permission>> updatePermission(@RequestBody PermissionDTO permissionDTO){

            return ResponseConfig.success(permissionService.updatePermission(permissionDTO));
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
