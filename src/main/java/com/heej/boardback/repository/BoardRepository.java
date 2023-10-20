package com.heej.boardback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heej.boardback.entity.BoardEntity;
import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

    boolean existsByBoardNumber(Integer boardNumber); 

    BoardEntity findByBoardNumber(Integer boardNumber);


    // 제약을 없앨 수 있다.
    // jpa에서 처리해줌
    // 쿼리에 적으면 많이 길어지고 계속 적어줘야 한다.
    // 한 번은 쿼리가 이득. 많은 비중을 차지하지 않을 때 사용. 두번, 세번 반복되면, 뷰 테이블로 작성해서 사용하는 것이 제일 좋다.
    // @Query(value=
    // "", 
    // nativeQuery=true)

    // 데이터베이스 자체에서 처리해줌. 
    // 가상테이블 뷰를 만들고 엔터티를 만들어 주면 됨.
    
}
