package com.ssafy.meshroom.backend.domain.user.application;

import com.ssafy.meshroom.backend.domain.user.dao.UserRepository;
import com.ssafy.meshroom.backend.domain.user.domain.User;
import com.ssafy.meshroom.backend.domain.user.domain.UserRole;
import com.ssafy.meshroom.backend.domain.user.dto.UserNameInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) {
        return userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException(id+" 사용자 없음"));
    }

    public String saveUser(String username, UserRole userRole){
        return userRepository.save(new User(username, userRole)).get_id();
    }

    public void updateUser(String userId, UserRole userRole){
        userRepository.updateById(userId, userRole);
    }

    public UserNameInfo getUserName(String userId) {
        return userRepository.findBy_id(userId);
    }
}
