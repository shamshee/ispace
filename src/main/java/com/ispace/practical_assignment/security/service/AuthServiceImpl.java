package com.ispace.practical_assignment.security.service;

import com.ispace.practical_assignment.entity.AppRole;
import com.ispace.practical_assignment.entity.Role;
import com.ispace.practical_assignment.entity.User;
import com.ispace.practical_assignment.repository.RoleRepository;
import com.ispace.practical_assignment.repository.UserRepository;
import com.ispace.practical_assignment.security.jwt.JwtUtils;
import com.ispace.practical_assignment.security.payload.LoginRequest;
import com.ispace.practical_assignment.security.payload.MessageResponse;
import com.ispace.practical_assignment.security.payload.SignupRequest;
import com.ispace.practical_assignment.security.payload.UserInfoResponse;
import com.ispace.practical_assignment.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CompromisedPasswordChecker compromisedPasswordChecker;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;



    @Override
    public ResponseEntity<?> signUp(SignupRequest signUpRequest) {


        CompromisedPasswordDecision check = compromisedPasswordChecker.check(signUpRequest.getPassword());

        if(check.isCompromised()){

            return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(),"Error: Password is Compromised Plz use strong password !");

//            return ResponseEntity.badRequest().body(new MessageResponse(HttpStatus.BAD_REQUEST.value(),
//                    "Error: Password is Compromised Plz use strong password !"));
        }

        if (userRepository.existsByUserName(signUpRequest.getUsername())) {

            return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(),"Error: Username is already taken!");
           //return ResponseEntity.badRequest().body(new MessageResponse(HttpStatus.BAD_REQUEST.value(),"Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {

            return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(),"Error: Email is already in use!");

           // return ResponseEntity.badRequest().body(new MessageResponse(HttpStatus.BAD_REQUEST.value(),"Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "seller":
                        Role modRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        return  ResponseUtil.created(savedUser,"User created Successfully");
}

    @Override
    public ResponseEntity<?> signIn(LoginRequest loginRequest) {

        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            return ResponseUtil.error(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserInfoResponse response = UserInfoResponse.builder()
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .jwtToken(jwtToken)
                .roles(roles)
         .build();


        return  ResponseUtil.success(response,"User Logged-in Successfully");

    }


}

