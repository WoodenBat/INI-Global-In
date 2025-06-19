package com.ini.security;

import com.ini.member.vo.MemberDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final MemberDTO member;

    public CustomUserDetails(MemberDTO member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 기본 권한 ROLE_USER 부여
        return Collections.singleton(() -> "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return member.getUser_password();
    }

    @Override
    public String getUsername() {
        return member.getUser_id(); // 또는 이메일 사용도 가능
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
