package com.em.reportserver.pos;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pos")
@PreAuthorize("hasRole('USER')")
public class PosController {



    private final PosServiceImpl posServiceImpl;

    @GetMapping
    public ResponseEntity<List<Pos>> getAll(Principal principal) {
        var response = posServiceImpl.getUserPos(principal.getName());
        return ResponseEntity.ok(response);
    }






}
