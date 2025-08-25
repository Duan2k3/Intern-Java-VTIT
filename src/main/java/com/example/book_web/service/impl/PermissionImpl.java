package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.permission.PermissionDTO;
import com.example.book_web.entity.Permission;
import com.example.book_web.entity.Role;
import com.example.book_web.entity.User;
import com.example.book_web.repository.PermissionRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.request.permission.PermissionRequest;
import com.example.book_web.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PermissionImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    /**
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public PermissionDTO createPermission(PermissionRequest request)  {
        Optional<Permission> existingPermission = permissionRepository.findByName(request.getName());
        if (existingPermission.isPresent()) {
            throw new DataExistingException("Permission existing","400");
        }
        Permission permission = Permission.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        permissionRepository.save(permission);
        return modelMapper.map(permission, PermissionDTO.class);

    }

    /**

     * @param request
     * @return
     */
    @Override
    public PermissionDTO updatePermission( Long id ,PermissionRequest request){
        Optional<Permission> existingPermission = permissionRepository.findById(id);
        if(existingPermission.isEmpty()){
            throw new DataNotFoundException("Permission khong ton tai","400");
        }
         Optional<Permission> findpermission = permissionRepository.findByName(request.getName());
        if(findpermission.isPresent()){
            throw new DataNotFoundException("Permission da ton tai","400");
        }
        Permission permission = existingPermission.get();

        modelMapper.map(request,permission);
       permissionRepository.save(permission);
        return modelMapper.map(permission,PermissionDTO.class);
    }

    /**
     * @param id
     */
    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);


    }

    /**
     * @return
     */
    @Override
    public List<PermissionDTO> getAllPermission() {
        return permissionRepository.getAllPermission();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<String> getDetailPermission(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new DataNotFoundException("User not existing","400");
        }
        User user = existingUser.get();

        List<Role> roles = user.getRoles();
        Set<String> permissions = new HashSet<>();
        for (Role role : roles) {
            List<Permission> rolePermissions = role.getPermissions();
            for (Permission permission : rolePermissions) {
                permissions.add(permission.getName());
            }
        }
        return new ArrayList<>(permissions);
    }

}
