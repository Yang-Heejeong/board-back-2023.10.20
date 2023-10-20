package com.heej.boardback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heej.boardback.entity.BoardViewEntity;

@Repository
public interface BoardViewRepository extends JpaRepository<BoardViewEntity, Integer> {

    BoardViewEntity findByBoardNumber(Integer boardNumber);

    List<BoardViewEntity> findByOrderByWriteDatetimeDesc();
    List<BoardViewEntity> findByWriterEmailOrderByWriteDatetimeDesc(String email);
    // 이후에 조회순, 댓글 순으로 추가할 수 있다.
    List<BoardViewEntity> findTop3ByWriteDatetimeGreaterThanOrderByFavoriteCountDesc(String writeDatetime);
    List<BoardViewEntity> findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(String title, String content);

    // List<BoardViewEntity> findByWriterEmail(String email);
}
