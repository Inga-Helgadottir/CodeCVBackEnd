package com.CodeCV.BackEnd.mapper;

import com.CodeCV.BackEnd.dto.RoleDTO;
import com.CodeCV.BackEnd.entity.Role;
import com.CodeCV.BackEnd.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleMapper {

    public RoleDTO mapToRoleDto(Role role){
        if(role != null){
            return new RoleDTO(
                    role.getId(),
                    role.getRole_name(),
                    role.getRole_description()
            );
        }else{
            throw new ResourceNotFoundException("The role provided is null");
        }
    }

    public List<RoleDTO> mapToRoleDtos(List<Role> roles){
        List<RoleDTO> roleDtos = new ArrayList<>();

        if(roles != null){
            if(!roles.isEmpty()){
                for(Role role : roles){
                    roleDtos.add(mapToRoleDto(role));
                }
            }
        }

        return roleDtos;
    }

    public Role mapToRole(RoleDTO roleDto){
        if(roleDto != null){
            return new Role(
                    roleDto.getRole_name(),
                    roleDto.getRole_description()
            );
        }else{
            throw new ResourceNotFoundException("The role provided is null");
        }
    }
}
