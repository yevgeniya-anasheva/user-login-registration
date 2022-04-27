package com.example.demo.registration;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import com.example.demo.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    //check if the email is valid, then invoke AppUserService to sign up the user
    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidator.test(request.getEmail());
        try {
            if(!isValidEmail) {
                throw new IllegalStateException("Email not valid");
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
        finally {
            System.out.println("hello from register() inside " + this.getClass());
        }

        return appUserService.signUpUser(
                new AppUser(request.getFirstName(), request.getLastName(),
                        request.getEmail(), request.getPassword(), AppUserRole.USER)
        );
    }
}
