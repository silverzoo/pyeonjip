package com.team5.pyeonjip.user.repository;

import com.team5.pyeonjip.user.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    // Refresh 토큰 이름으로 조회
    Boolean existsByRefresh(String refresh);

    // Refresh 토큰 이름으로 토큰 삭제
    @Transactional
    void deleteByRefresh(String refresh);
}
