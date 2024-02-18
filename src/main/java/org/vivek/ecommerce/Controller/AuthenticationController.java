package org.vivek.ecommerce.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.vivek.ecommerce.DTO.AuthenticationRequest;
import org.vivek.ecommerce.DTO.AuthenticationResponse;
import org.vivek.ecommerce.DTO.RegisterRequest;
import org.vivek.ecommerce.Models.Role;
import org.vivek.ecommerce.Services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final JavaMailSender javaMailSender;
    @PostMapping("/buyer/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        try{
            AuthenticationResponse authenticationResponse = userService.register(request, Role.BUYER_ROLE);

            return ResponseEntity.ok(authenticationResponse);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request){
        try{
            return ResponseEntity.ok(userService.authenticate(request));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/confirm-email")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        try{
            return userService.confirmEmail(confirmationToken);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
