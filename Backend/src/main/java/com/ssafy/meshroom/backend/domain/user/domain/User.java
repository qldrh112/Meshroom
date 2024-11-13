package com.ssafy.meshroom.backend.domain.user.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Setter
@Document("users")
@Component
public class User implements UserDetails {
    @Getter
    @Id
    @Indexed(unique = true)
    private String _id;
    private String username;
    private UserRole role;  // UserRole 추가

    // 기본 생성자
    public User() {
    }

    // 모든 필드를 포함한 생성자
    public User(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return null;  // token을 password로 사용. 실제 환경에서는 별도의 암호화된 비밀번호 필드를 사용해야 합니다.
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}