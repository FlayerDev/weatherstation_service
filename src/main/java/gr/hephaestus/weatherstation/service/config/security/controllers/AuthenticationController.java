package gr.hephaestus.weatherstation.service.config.security.controllers;

/*
*
*  @Code from GitHub repository :  https://github.com/xartokoptiko/spring-jwt-conf
*
*/

import gr.hephaestus.weatherstation.service.config.security.dto.LoginResponse;
import gr.hephaestus.weatherstation.service.config.security.dto.LoginUserDto;
import gr.hephaestus.weatherstation.service.config.security.dto.RegisterUserDto;
import gr.hephaestus.weatherstation.service.config.security.entities.User;
import gr.hephaestus.weatherstation.service.config.security.entities.VerificationToken;
import gr.hephaestus.weatherstation.service.config.security.repositories.UserRepository;
import gr.hephaestus.weatherstation.service.config.security.repositories.VerificationTokenRepository;
import gr.hephaestus.weatherstation.service.config.security.services.AuthenticationService;
import gr.hephaestus.weatherstation.service.config.security.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    UserRepository userRepository;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        if (authenticatedUser.isEnabled()) {
            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());

            return ResponseEntity.ok(loginResponse);
        }

        return ResponseEntity.status(401).body("Account not activated");
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("token") String token) {
        try {
            VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid or expired activation token."));

            User user = verificationToken.getUser();
            if (user.isEnabled()) {
                return ResponseEntity.ok(buildHtmlResponse("Your account is already activated 🚀"));
            }

            user.setEnabled(true);
            userRepository.save(user);
            verificationTokenRepository.delete(verificationToken);

            return ResponseEntity.ok(buildHtmlResponse("🎉 Your account has been activated successfully!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(buildHtmlResponse("❌ Activation failed: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(buildHtmlResponse("⚠️ Oops! Something went wrong."));
        }
    }

    private String buildHtmlResponse(String message) {
        return """
        <html>
            <head>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        text-align: center;
                        padding: 40px;
                        background-color: #f4f4f4;
                        color: #333;
                    }
                    .container {
                        background: white;
                        padding: 30px;
                        border-radius: 10px;
                        box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        display: inline-block;
                    }
                    h1 {
                        color: #4CAF50;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>%s</h1>
                </div>
            </body>
        </html>
        """.formatted(message);
    }

}
