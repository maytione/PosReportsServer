package com.em.reportserver.pos;

import com.em.reportserver.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class PosServiceImpl implements PosService {

    private final UserRepository userRepository;

    private final PosRepository posRepository;

    public Pos createPos(PosDto posDto) {

        var pos = Pos.builder()
                .name(posDto.getName())
                .code(posDto.getCode())
                .build();

        posRepository.save(pos);

        return pos;

    }

    public List<Pos> getAllPos() {

        return posRepository.findAll();

    }

    public List<Pos> getUserPos(String username) {

        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return user.get().getPosList();

    }

    @Transactional
    public void assignPosToUser(Long posId, Long userId) {
        var pos = posRepository.findById(posId);
        if (pos.isEmpty()) {
            throw new RuntimeException("POS not found");
        }
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        pos.get().setUser(user.get());

    }

    @Transactional
    public void updatePos(Long posId, PosDto posDto) {
        var pos = posRepository.findById(posId);
        if (pos.isEmpty()) {
            throw new RuntimeException("POS not found");
        }
        var updated = pos.get();
        updated.setCode(posDto.getCode());
        updated.setName(posDto.getName());
    }

    public void deletePos(Long posId) {
        posRepository.deleteById(posId);
    }
}
