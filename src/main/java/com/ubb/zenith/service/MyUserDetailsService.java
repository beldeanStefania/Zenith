//package com.ubb.zenith.service;
//
//import com.ubb.zenith.model.MyUserDetails;
//import com.ubb.zenith.model.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import com.ubb.zenith.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository repository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = repository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new MyUserDetails(user);
//    }
//}
