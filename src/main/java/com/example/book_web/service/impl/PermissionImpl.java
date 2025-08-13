package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.PermissionDTO;
import com.example.book_web.entity.Permission;
import com.example.book_web.entity.Role;
import com.example.book_web.entity.User;
import com.example.book_web.repository.PermissionRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    /**
     * @param permissionDTO
     * @return
     * @throws Exception
     */
    @Override
    public Permission createPermission(PermissionDTO permissionDTO)  {
        Optional<Permission> existingPermission = permissionRepository.findByName(permissionDTO.getName());
        if (existingPermission.isPresent()) {
            throw new DataExistingException("Permission existing","400");
        }
        Permission permission = Permission.builder()
                .name(permissionDTO.getName())
                .description(permissionDTO.getDescription())
                .build();
       return permissionRepository.save(permission);

    }

    /**

     * @param permissionDTO
     * @return
     */
    @Override
    public Permission updatePermission( PermissionDTO permissionDTO){
        Optional<Permission> existingPermission = permissionRepository.findById(permissionDTO.getId());
        if(existingPermission.isEmpty()){
            throw new DataNotFoundException("Permission khong ton tai","400");
        }
         Optional<Permission> findpermission = permissionRepository.findByName(permissionDTO.getName());
        if(findpermission.isPresent()){
            throw new DataNotFoundException("Permission da ton tai","400");
        }
        Permission permission = existingPermission.get();

        modelMapper.map(permissionDTO,permission);


       return permissionRepository.save(permission);
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
