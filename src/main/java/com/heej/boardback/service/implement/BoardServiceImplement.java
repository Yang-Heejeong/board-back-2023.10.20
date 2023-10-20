package com.heej.boardback.service.implement;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.heej.boardback.dto.request.board.PatchBoardRequestDto;
import com.heej.boardback.dto.request.board.PostBoardRequestDto;
import com.heej.boardback.dto.request.board.PostCommentRequestDto;
import com.heej.boardback.dto.response.ResponseDto;
import com.heej.boardback.dto.response.board.DeleteBoardResponseDto;
import com.heej.boardback.dto.response.board.GetBoardResponseDto;
import com.heej.boardback.dto.response.board.GetCommentListResponseDto;
import com.heej.boardback.dto.response.board.GetFavoriteListResponseDto;
import com.heej.boardback.dto.response.board.GetLatestBoardListResponseDto;
import com.heej.boardback.dto.response.board.GetSearchBoardListResponseDto;
import com.heej.boardback.dto.response.board.GetTop3BoardListResponseDto;
import com.heej.boardback.dto.response.board.GetUserBoardListResponseDto;
import com.heej.boardback.dto.response.board.IncreaseViewCountResponseDto;
import com.heej.boardback.dto.response.board.PatchBoardResponseDto;
import com.heej.boardback.dto.response.board.PostBoardResponseDto;
import com.heej.boardback.dto.response.board.PostCommentResponseDto;
import com.heej.boardback.dto.response.board.PutFavoriteResponseDto;
import com.heej.boardback.entity.BoardEntity;
import com.heej.boardback.entity.BoardImageEntity;
import com.heej.boardback.entity.BoardViewEntity;
import com.heej.boardback.entity.CommentEntity;
import com.heej.boardback.entity.FavoriteEntity;
import com.heej.boardback.entity.SearchLogEntity;
import com.heej.boardback.entity.UserEntity;
import com.heej.boardback.repository.BoardImageRepository;
import com.heej.boardback.repository.BoardRepository;
import com.heej.boardback.repository.BoardViewRepository;
import com.heej.boardback.repository.CommentRepository;
import com.heej.boardback.repository.FavoriteRepository;
import com.heej.boardback.repository.SearchLogRepository;
import com.heej.boardback.repository.UserRepository;
import com.heej.boardback.repository.resultSet.CommentListResultSet;
import com.heej.boardback.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplement implements BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final BoardViewRepository boardViewRepository;
    private final SearchLogRepository searchLogRepository;
    private final BoardImageRepository boardImageRepository;

    @Override
    public ResponseEntity<? super PostBoardResponseDto> postBoard(PostBoardRequestDto dto, String email) {
        
        try {

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PostBoardResponseDto.notExistUser();

            BoardEntity boardEntity = new BoardEntity(dto, email);
            boardRepository.save(boardEntity);

            List<String> boardImageList = dto.getBoardImageList();
            Integer boardNumber = boardEntity.getBoardNumber();

            List<BoardImageEntity> boardImageEntities = new ArrayList<>();
            for (String boardImage: boardImageList) {
                BoardImageEntity boardImageEntity = new BoardImageEntity(boardNumber, boardImage);
                boardImageEntities.add(boardImageEntity);
            }

            boardImageRepository.saveAll(boardImageEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostBoardResponseDto.success();

    }

    @Override
    public ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer boardNumber, String email) {
        
        try {

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PostCommentResponseDto.notExistBoard();

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PostCommentResponseDto.notExistUser();

            CommentEntity commentEntity = new CommentEntity(dto, boardNumber, email);
            commentRepository.save(commentEntity);

            boardEntity.increaseCommentCount();
            boardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostCommentResponseDto.success();
    }

	@Override
	public ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber) {

        BoardViewEntity boardViewEntity = null;
        List<BoardImageEntity> boardImageEntities = new ArrayList<>();

        try {

            boardViewEntity = boardViewRepository.findByBoardNumber(boardNumber);
            if (boardViewEntity == null) return GetBoardResponseDto.notExistBoard();

            boardImageEntities = boardImageRepository.findByBoardNumber(boardNumber);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetBoardResponseDto.success(boardViewEntity, boardImageEntities);

	}

    @Override
    public ResponseEntity<? super GetFavoriteListResponseDto> getFavorietList(Integer boardNumber) {

        List<UserEntity> userEntities = new ArrayList<>();

        try {

            boolean existedBoard = boardRepository.existsByBoardNumber(boardNumber);
            if (!existedBoard) return GetFavoriteListResponseDto.notExistBoard();

            userEntities = userRepository.findByBoardFavorite(boardNumber);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFavoriteListResponseDto.success(userEntities);

    }

    @Override
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer boardNumber) {

        List<CommentListResultSet> resultSets = new ArrayList<>();

        try {

            boolean existedBoard = boardRepository.existsByBoardNumber(boardNumber);
            if (!existedBoard) return GetCommentListResponseDto.notExistBoard();
            
            resultSets = commentRepository.findByCommentList(boardNumber);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        
        return GetCommentListResponseDto.success(resultSets);

    }

    @Override
    public ResponseEntity<? super GetLatestBoardListResponseDto> getLatestBoardList() {

        List<BoardViewEntity> boardViewEntities = new ArrayList<>();
        
        try {

            boardViewEntities = boardViewRepository.findByOrderByWriteDatetimeDesc();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetLatestBoardListResponseDto.success(boardViewEntities);

    }

    @Override
    public ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(String email) {
        
        List<BoardViewEntity> boardViewEntities = new ArrayList<>();

        try {

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return GetUserBoardListResponseDto.notExistUser();

            boardViewEntities = boardViewRepository.findByWriterEmailOrderByWriteDatetimeDesc(email);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUserBoardListResponseDto.success(boardViewEntities);
    }
    
    @Override
    public ResponseEntity<? super GetTop3BoardListResponseDto> getTop3BoardList() {

        List<BoardViewEntity> boardViewEntities = new ArrayList<>();

        try {

            Date now = Date.from(Instant.now().minus(7, ChronoUnit.DAYS));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy=MM-dd HH:mm:ss");
            String sevenDaysAgo = simpleDateFormat.format(now);

            boardViewEntities = boardViewRepository.findTop3ByWriteDatetimeGreaterThanOrderByFavoriteCountDesc(sevenDaysAgo);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetTop3BoardListResponseDto.success(boardViewEntities);

    }
    
    @Override
    public ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardList(String searchWord, String preSearchWord) {

        List<BoardViewEntity> boardViewEntities = new ArrayList<>();

        try {

            boardViewEntities = boardViewRepository.findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(searchWord, searchWord);

            boolean relation = preSearchWord != null;

            SearchLogEntity searchLogEntity = new SearchLogEntity(searchWord, preSearchWord, false);
            searchLogRepository.save(searchLogEntity);

            if (relation) {
                searchLogEntity = new SearchLogEntity(preSearchWord, searchWord, relation);
                searchLogRepository.save(searchLogEntity);
            }
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetSearchBoardListResponseDto.success(boardViewEntities);

    }

    @Override
    public ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer boardNumber, String email) {
        
        try {

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PutFavoriteResponseDto.notExistBoard();

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PutFavoriteResponseDto.notExistUser();

            boolean isFavorite = favoriteRepository.existsByUserEmailAndBoardNumber(email, boardNumber);

            FavoriteEntity favoriteEntity = new FavoriteEntity(email, boardNumber);
            
            if (isFavorite) {
                favoriteRepository.delete(favoriteEntity);
                boardEntity.decreaseFavoriteCount();
            }
            else {
                favoriteRepository.save(favoriteEntity);
                boardEntity.increaseFavoriteCount();
            }

            boardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PutFavoriteResponseDto.success();

    }

    @Override
    public ResponseEntity<? super PatchBoardResponseDto> patchBoard(PatchBoardRequestDto dto, Integer boardNumber,String email) {
        
        try {

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PatchBoardResponseDto.notExistUser();

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PatchBoardResponseDto.notExistBoard();

            boolean equalWriter = boardEntity.getWriterEmail().equals(email);
            if (!equalWriter) return PatchBoardResponseDto.noPermission();

            boardEntity.patch(dto);
            boardRepository.save(boardEntity);

            List<String> boardImageList = dto.getBoardImageList();

            boardImageRepository.deleteByBoardNumber(boardNumber);
            
            List<BoardImageEntity> boardImageEntities = new ArrayList<>();
            for (String boardImage: boardImageList) {
                BoardImageEntity boardImageEntity = new BoardImageEntity(boardNumber, boardImage);
                boardImageEntities.add(boardImageEntity);
            }
            boardImageRepository.saveAll(boardImageEntities);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PatchBoardResponseDto.success();

    }

    @Override
    public ResponseEntity<? super IncreaseViewCountResponseDto> increaseViewCount(Integer boardNumber) {
        
        try {

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return IncreaseViewCountResponseDto.notExistBoard();

            boardEntity.increaseViewCount();
            boardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return IncreaseViewCountResponseDto.success();

    }

    @Override
    public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Integer boardNumber, String email) {

        try {

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return DeleteBoardResponseDto.notExistUser();

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return DeleteBoardResponseDto.notExistBoard();

            boolean isWriter = boardEntity.getWriterEmail().equals(email);
            if (!isWriter) return DeleteBoardResponseDto.noPermission();

            // 테이블에 제약 조건이 걸려 있다면 굳이 사용하지 않아도 된다.
            commentRepository.deleteByBoardNumber(boardNumber);
            favoriteRepository.deleteByBoardNumber(boardNumber);
            boardImageRepository.deleteByBoardNumber(boardNumber);
            // 참조하는 테이블이 있다면 delete가 제대로 실행되지 않음.
            boardRepository.delete(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return DeleteBoardResponseDto.success();

    }

    
}