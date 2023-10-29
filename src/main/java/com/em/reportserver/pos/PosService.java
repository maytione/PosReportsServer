package com.em.reportserver.pos;

import java.util.List;

public interface PosService {
    Pos createPos(PosDto posDto);
    List<Pos> getAllPos();
    List<Pos> getUserPos(String username);
    void assignPosToUser(Long posId, Long userId);
    void updatePos(Long posId, PosDto posDto);
    void deletePos(Long posId);
}
