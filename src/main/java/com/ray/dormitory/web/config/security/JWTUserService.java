package com.ray.dormitory.web.config.security;

import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class JWTUserService implements UserDetailsService {
    private final UserService userService;
    public JWTUserService(UserService userService){
        this.userService=userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByAccount(username);
        if (user==null){
            throw new UsernameNotFoundException("");
        }
        JWTUser jwtUser = new JWTUser();
        jwtUser.setUsername(user.getName());
        jwtUser.setPassword(user.getPassword());
        jwtUser.setAuthorities(user.getRoleIds()
                .stream()
                .map(roleId->new SimpleGrantedAuthority(String.valueOf(roleId)))
                .collect(Collectors.toList()));

        return jwtUser;
    }
}
