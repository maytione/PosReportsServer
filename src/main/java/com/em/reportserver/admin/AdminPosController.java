package com.em.reportserver.admin;

import com.em.reportserver.pos.Pos;
import com.em.reportserver.pos.PosDto;
import com.em.reportserver.pos.PosResponse;
import com.em.reportserver.pos.PosServiceImpl;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/pos")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPosController {


    private final PosServiceImpl posServiceImpl;

    private final ModelMapper modelMapper;


    @GetMapping("")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<PosResponse>> getAllPos() {
        List<PosResponse> response = posServiceImpl.getAllPos()
                .stream()
                .map(item -> modelMapper.map(item, PosResponse.class))
                .toList();

        return ResponseEntity.ok(response);
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<PosResponse> createPos(@RequestBody PosDto posDto) {
        Pos pos = posServiceImpl.createPos(posDto);
        PosResponse posResponse = modelMapper.map(pos, PosResponse.class);
        return ResponseEntity.ok(posResponse);
    }

    @PutMapping("/{pos_id}/{user_id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<String> assignPosToUser(@PathVariable Long pos_id, @PathVariable Long user_id) {
        posServiceImpl.assignPosToUser(pos_id, user_id);
        return ResponseEntity.ok("Assigned");

    }

    @PutMapping("/{pos_id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<String> updatePos(@PathVariable Long pos_id, @RequestBody PosDto posDto) {
        posServiceImpl.updatePos(pos_id, posDto);
        return ResponseEntity.ok("Updated");

    }

    @DeleteMapping("/{pos_id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<String> deletePos(@PathVariable Long pos_id) {
        posServiceImpl.deletePos(pos_id);
        return ResponseEntity.ok("Deleted");

    }


}
