package security.instagram.controller.mapper;

import org.springframework.stereotype.Component;
import security.instagram.dto.UserDto;
import security.instagram.dto.UserProfile;
import security.instagram.entity.User;

/**
 * @author ket_ein17
 * @Date 5/27/2024
 */
@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        //Optional<Role> role = roleRepository.findById(user.getRoleId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
//        userDto.setGender(user.getGender());
        return userDto;
    }

    public UserProfile showProfile(User user){
//        UserAdditionalInfo userAdditionalInfo = user.getInfo();
        String dateOfBirthStr = user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : null; // null-safe
        UserProfile userProfile = UserProfile.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(dateOfBirthStr)
//                .gender(user.getGender())
//                .accountType(user.getAccountType())
                .avatarId(user.getAvatarId())
//                .userAdditionalInfoProfile(userAdditionalInfoMapper.toUserAdditionalDto(userAdditionalInfo))
                .build();
        return userProfile;
    }
}
