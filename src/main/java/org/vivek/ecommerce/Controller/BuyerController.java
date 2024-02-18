package org.vivek.ecommerce.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/buyer")
public class BuyerController {
    @GetMapping("/hello")
    public ResponseEntity<String> sayHelloToBuyer(){
        return ResponseEntity.ok("Buyer is saying hello");
    }


}
