package com.example.book_web.controller;

import com.example.book_web.components.LocalizationUtils;
import com.example.book_web.dto.PermissionDTO;
import com.example.book_web.entity.Permission;
import com.example.book_web.service.PermissionService;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ROLE_CREATE_PERMISSION')")
    public ResponseEntity<?> createPermission(@RequestBody PermissionDTO permissionDTO) throws Exception{
        try {
            permissionService.createPermission(permissionDTO);
            return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERMISSION')")
    public ResponseEntity<?> updatePermission(@PathVariable Long id , @RequestBody PermissionDTO permissionDTO) throws Exception{
        try {
            permissionService.updatePermission(id,permissionDTO);
            return ResponseEntity.ok("Sua thanh cong");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_PERMISSION')")
    public ResponseEntity<?> deletePermission(@PathVariable Long id){
        permissionService.deletePermission(id);
      return   ResponseEntity.ok("Delete thanh cong");

    }
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_VIEW_PERMISSION')")
    public  ResponseEntity<List<PermissionDTO>> findAllPermission(){
      List<PermissionDTO> permissions = permissionService.getAllPermission();
        return ResponseEntity.ok(permissions);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_PERMISSION')")
    public  ResponseEntity<List<String>> getDetailPermissionUser(@PathVariable Long id) throws Exception{
        try {
            List<String> permissions = permissionService.getDetailPermission(id);
            return ResponseEntity.ok(permissions);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(e.getMessage()));
        }
    }


}
