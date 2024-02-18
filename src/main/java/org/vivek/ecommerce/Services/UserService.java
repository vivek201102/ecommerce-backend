package org.vivek.ecommerce.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.vivek.ecommerce.DTO.AuthenticationRequest;
import org.vivek.ecommerce.DTO.AuthenticationResponse;
import org.vivek.ecommerce.DTO.RegisterRequest;
import org.vivek.ecommerce.Models.Role;
import org.vivek.ecommerce.Models.User;

import java.util.Optional;


public interface UserService {

    AuthenticationResponse register(RegisterRequest request, Role role);

    AuthenticationResponse authenticate(AuthenticationRequest request);
    ResponseEntity<?> confirmEmail(String confirmationToken);
}
