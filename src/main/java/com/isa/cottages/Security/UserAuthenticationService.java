package com.isa.cottages.Security;

import com.isa.cottages.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import static com.isa.cottages.Model.UserRole.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuthenticationService implements AuthenticationProvider {

    private UserRepository userRepository;

    @Autowired
    public UserAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        System.out.println("test");
        Authentication retVal = null;
        List<GrantedAuthority> grantedAuths = new ArrayList<>();

        if (auth != null) {
            String email = auth.getName();
            String password = auth.getCredentials().toString();
            System.out.println("email: " + email);
            System.out.println("password: " + password);

        if (userRepository.findByEmail(email).getUserRole() == SYS_ADMIN) {
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_SYS_ADMIN"));

            retVal = new UsernamePasswordAuthenticationToken(
                    email, "", grantedAuths
            );
        }
        else if (userRepository.findByEmail(email).getUserRole() == COTTAGE_OWNER) {
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_COTTAGE_OWNER"));

            retVal = new UsernamePasswordAuthenticationToken(
                    email, "", grantedAuths
            );
        } else if (userRepository.findByEmail(email).getUserRole() == BOAT_OWNER) {
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_BOAT_OWNER"));

            retVal = new UsernamePasswordAuthenticationToken(
                    email, "", grantedAuths
            );
        }
    }
    else

    {
        System.out.println("invalid login");
        retVal = new UsernamePasswordAuthenticationToken(
                null, null, grantedAuths
        );
        System.out.println("bad Login");
    }
    System.out.println("return login info");
    return retVal;
}
    @Override
    public boolean supports(Class<?> tokenType) {
        return tokenType.equals(UsernamePasswordAuthenticationToken.class);
    }
}
