package com.example.demo.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        try {
            if(userExists) {
                throw new IllegalStateException("Email already taken");
            }
            else {
                String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
                appUser.setPassword(encodedPassword);
                appUserRepository.save(appUser);
            }
        }
        catch (IllegalStateException ex) {
            System.out.println("IllegalStateException: " + ex.getMessage());
            ex.printStackTrace();
        }
        catch (Exception ex) {
            System.out.println("Unknown error " + ex.getMessage() + " from " + ex.getClass());
            ex.printStackTrace();
        }
        //TODO: Send confirmation token
        return "signUpUser() from " + this.getClass();
    }
}
