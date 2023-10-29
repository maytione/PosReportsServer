package com.em.reportserver.pos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PosRepository extends JpaRepository<Pos, Long> {

    Optional<Pos> findByUserIdAndCode(Long userId, String code);

}
