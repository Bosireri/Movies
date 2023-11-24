package com.series.movies.service;

import com.series.movies.model.Admin;
import com.series.movies.model.LocalUser;
import com.series.movies.model.UserType;
import com.series.movies.model.dao.AdminRepository;
import com.series.movies.model.dao.LocalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserTypeService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LocalUserRepository localUserRepository;

    private UserType userType;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(userType);

        if(userType == UserType.ADMIN) {
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Admin Username" + username + "not found"));
            SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(UserType.ADMIN.toString());
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(adminAuthority);
            return new User(admin.getUsername(), admin.getPassword(), authorities);

        } else if (userType == UserType.LOCALUSER) {
            LocalUser localUser = localUserRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("LocalUser  PhoneNumber"+ username + "not found"));
            SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(UserType.LOCALUSER.toString());
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(adminAuthority);
            return new User(localUser.getEmail(), localUser.getPassword(), authorities);
        }
        return null;
    }
}
