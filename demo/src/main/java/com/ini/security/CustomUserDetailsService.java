package com.ini.security;

import com.ini.member.mapper.MemberMapper;
import com.ini.member.vo.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	System.out.println(new BCryptPasswordEncoder().encode("test"));
        MemberDTO member = memberMapper.findMemberById(username);

        if (member == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + username);
        }

        return new CustomUserDetails(member);
    }
}
