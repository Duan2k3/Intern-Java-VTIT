package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.role.RoleDTO;
import com.example.book_web.entity.Permission;
import com.example.book_web.entity.Role;
import com.example.book_web.entity.User;
import com.example.book_web.repository.PermissionRepository;
import com.example.book_web.repository.RoleRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.request.role.RoleRequest;
import com.example.book_web.response.RoleDetailResponse;
import com.example.book_web.response.RoleResponse;
import com.example.book_web.service.RoleService; // Thêm interface
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAllRoles();
    }

    /**
     * @param
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public RoleDTO createRole(RoleRequest request) {
        Role role = request.getRole();
        roleRepository.save(role);
        List<User> users = new ArrayList<>();
        for (Long userId : request.getUserIds()) {
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isEmpty()) {
                throw new DataNotFoundException("User with ID " + userId + " not found.","400");
            }
            users.add(existingUser.get());
        }
       List<Permission> permissions = new ArrayList<>();
        for (Long permissionId : request.getPermissionIds()) {
            Optional<Permission> existingPermission = permissionRepository.findById(permissionId);
            if (existingPermission.isEmpty()) {
                throw new DataNotFoundException("Permission with ID " + permissionId + " not found.","400");
            }
            permissions.add(existingPermission.get());
        }
        role.setPermissions(permissions);
        role.setUsers(users);
       roleRepository.save(role);
       return modelMapper.map(role, RoleDTO.class);
    }


    @Override
    @Transactional(readOnly = true)
    public RoleDetailResponse getRoleDetail(Long id) {
        Role role =  roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        return RoleDetailResponse.builder()
                .name(role.getName())
                .build();
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Long id,RoleRequest request){
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Role not found with id: " + id,"400"));


        if (request.getRole().getName() != null && !request.getRole().getName().trim().isEmpty()) {
            if (request.getRole().getName().equals(existingRole.getName()) ) {
                throw new IllegalStateException("Role name '" +request.getRole().getName() + "' is already taken");
            }
            existingRole.setName(request.getRole().getName());
        }
        existingRole.getUsers().clear();
        existingRole.getPermissions().clear();
        List<User> users = new ArrayList<>();
        for (Long userId : request.getUserIds()) {
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isEmpty()) {
                throw new DataNotFoundException("User with ID " + userId + " not found.","400");
            }
            users.add(existingUser.get());
        }
        List<Permission> permissions = new ArrayList<>();
        for (Long permissionId : request.getPermissionIds()) {
            Optional<Permission> existingPermission = permissionRepository.findById(permissionId);
            if (existingPermission.isEmpty()) {
                throw new DataNotFoundException("Permission with ID " + permissionId + " not found.","400");
            }
            permissions.add(existingPermission.get());
        }
        existingRole.setPermissions(permissions);
        existingRole.setUsers(users);

        roleRepository.save(existingRole);
        return modelMapper.map(existingRole, RoleDTO.class);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new DataNotFoundException("Role not found with id: " + id,"400");
        }
        roleRepository.deleteById(id);
    }
}