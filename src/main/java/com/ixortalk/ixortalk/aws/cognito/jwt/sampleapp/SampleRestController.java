package com.ixortalk.ixortalk.aws.cognito.jwt.sampleapp;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SampleRestController {

    @GetMapping("/api/me")
    public Principal me(Principal principal) {
        return principal;
    }

    @GetMapping("/api/supersecure")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Principal superSecure(Principal principal) {
        return principal;
    }

    @GetMapping("/api/secure")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Principal secure(Principal principal) {
        return principal;
    }

}
