package com.querotattoo.entities.dto;

import com.querotattoo.entities.Role;
import com.querotattoo.entities.TattooEstimate;
import com.querotattoo.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class DTOMapper {

    public UserReadDTO toDto(User user) {
        String name = user.getName();
        List<String> roles = user
                .getRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(toList());

        return new UserReadDTO(user, roles);
    }

    public TattooEstimateReadDTO toDto(TattooEstimate estimate) {
        return new TattooEstimateReadDTO(estimate);
    }


//    public User toUser(UserCreationDTO userDTO) {
//        return new User(userDTO.getName(), userDTO.getPassword(), new ArrayList<>());
//    }
}