package com.em.reportserver.admin;

import com.em.reportserver.auth.AuthenticationResponse;
import com.em.reportserver.auth.AuthenticationService;


import com.em.reportserver.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/user")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {


    private final AuthenticationService authenticationService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> createPos(@RequestBody UserDto userDto) {
        AuthenticationResponse response = authenticationService.register(userDto);
        return ResponseEntity.ok(response);
    }

}
