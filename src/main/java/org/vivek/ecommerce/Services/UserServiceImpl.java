package org.vivek.ecommerce.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vivek.ecommerce.DTO.AuthenticationRequest;
import org.vivek.ecommerce.DTO.AuthenticationResponse;
import org.vivek.ecommerce.DTO.RegisterRequest;
import org.vivek.ecommerce.Models.ConfirmationToken;
import org.vivek.ecommerce.Models.Role;
import org.vivek.ecommerce.Models.User;
import org.vivek.ecommerce.Repository.ConfirmationTokenRepository;
import org.vivek.ecommerce.Repository.UserRepository;
import org.vivek.ecommerce.Security.JwtServices;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServices jwtServices;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public AuthenticationResponse register(RegisterRequest request, Role role) {

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isVerified(false)
                .build();

        userRepository.save(user);
        var jwtToken = jwtServices.generateToken(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Verify you email address");
        simpleMailMessage.setText("Welcome to Viv E-Commerce\nPlease verify your email address to start using your account.\nVerify here: http://localhost:8080/api/v1/auth/confirm-email?token=" + confirmationToken.getConfirmationToken());
        emailService.sendEmail(simpleMailMessage);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtServices.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findConfirmationTokenByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findByEmail(token.getUser().getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            user.setVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
}
