//package com.hms.user.jwt;
//
//import com.hms.user.dto.UserDTO;
//import com.hms.user.exception.HmsException;
//import com.hms.user.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        try{
//            UserDTO dto = userService.getUser(email);
//            System.out.println("EMAIL FROM DB = " + dto.getEmail());
//            System.out.println("PASSWORD FROM DB = " + dto.getPassword());
//            System.out.println("PASSWORD STARTS WITH $2a$ ? " + dto.getPassword().startsWith("$2"));
//            List<GrantedAuthority> authorities =
//                    List.of(new SimpleGrantedAuthority("ROLE_" + dto.getRole().name()));
//            return new CustomUserDetails(
//                    dto.getId(),
//                    dto.getEmail(),
//                    dto.getPassword(),
//                    dto.getRole(),
//                    dto.getName(),
//                    dto.getEmail(),
//                    authorities);
//        }
//        catch(HmsException e){
//            throw new UsernameNotFoundException("USER_NOT_FOUND",e);
//        }
//    }
//}
