package com.ulalala.board_jpa.service;

import com.ulalala.board_jpa.dao.repository.BoardRepository;
import com.ulalala.board_jpa.dao.repository.FileRepository;
import com.ulalala.board_jpa.domain.Board;
import com.ulalala.board_jpa.domain.FileEntity;
import com.ulalala.board_jpa.domain.Paging;
import com.ulalala.board_jpa.dto.board.*;
import com.ulalala.board_jpa.dto.common.PageInfo;
import com.ulalala.board_jpa.dto.file.FileRequest;
import com.ulalala.board_jpa.dto.file.FileResponse;
import com.ulalala.board_jpa.dto.user.UserSession;
import com.ulalala.board_jpa.common.response.CommonResponse;
import com.ulalala.board_jpa.common.response.ResponseStatus;
import com.ulalala.board_jpa.common.response.SuccessMessage;
import com.ulalala.board_jpa.common.response.exception.ErrorCode;
import com.ulalala.board_jpa.common.response.exception.TicketingException;
import com.ulalala.board_jpa.util.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class BoardService {
    private final FTPFileService fileService;
    private final CommentService commentService;
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;



    public BoardService(FTPFileService fileService, CommentService commentService, BoardRepository boardRepository, FileRepository fileRepository) {
        this.fileService = fileService;
        this.commentService = commentService;
        this.boardRepository = boardRepository;
        this.fileRepository = fileRepository;
    }

    public CommonResponse post(BoardRequest boardRequest, HttpServletRequest request) {

        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");

        if (userSession != null) {
            Board board = new Board(null, boardRequest.getTitle(), boardRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null);
            boardRepository.save(board);
            boardRequest.setBId(board.getBoardNo());

            Integer bIdx = boardRequest.getBId();
            if (boardRequest.getFile() != null) {
                try {
                    fileService.uploadFiletoFtp(boardRequest); // 파일 업로드
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new TicketingException(ErrorCode.INVALID_LOGIN);
        }
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "게시물 " + SuccessMessage.SUCCESS_CREATE.getMessage(), null);
    }

    // 페이지
    public List<BoardResponse> selectAllPosts(String searchType, String keyword, int curPage, int pageSize, int blockSize) {
//        BoardSearchRequest search = new BoardSearchRequest(searchType, keyword, curPage, pageSize, blockSize, boardMapper.getTotalCnt());
//        List<BoardResponse> list = boardMapper.selectPost(search);
//        return list;
        return null;
    }

    public PageInfo getPagingInfo(int page, int size, int blockSize) {
//        Paging paging = new Paging(page, size, blockSize, boardMapper.getTotalCnt());
//        PageInfo pageInfo = new PageInfo(paging);
//        return pageInfo;
        return null;
    }

    public BoardInfoResponse selectPostByPostId(Integer bIdx) {
        FileEntity file = fileRepository.findFileByBoardNo(bIdx);
        FileResponse fileResponse;
        if (file == null) fileResponse = null;
        else fileResponse = new FileResponse(file.getFileNo(), file.getFileName(), file.getPath()); // null

        BoardInfoResponse boardInfo =
                new BoardInfoResponse(
                        boardRepository.findById(bIdx).orElseThrow(IllegalAccessError::new),
                        fileResponse,
                        commentService.selectCommentsByPostId(bIdx));

        return boardInfo;
    }

    // 내 게시물 - 사용자 검색
    public List<BoardResponse> selectPostsByUserId(int page, int size, int blockSize) {
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");

//        if (userSession != null) {
//            Paging paging = new Paging(page, size, blockSize, boardMapper.getTotalCnt());
//            Map<String, Integer> map = new HashMap<>();
//            map.put("startIndex", paging.getStartIndex());
//            map.put("endIndex", paging.getEndIndex());
//            map.put("id", userSession.getIdx());
//            List<BoardResponse> list = boardMapper.selectPostsByUserId(map);
//            return list;
//        } else {
//            throw new TicketingException(ErrorCode.INVALID_LOGIN);
//        }
        return null;
    }

    public CommonResponse updatePost(int bIdx, BoardRequest boardRequest) {
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");
        Optional<Board> board = boardRepository.findById(bIdx);

        if (board == null) {
            throw new TicketingException(ErrorCode.INVALID_BOARD);
        }

        Integer b_uidx = board.get().getUserIdx();
        BoardDto boardDto = new BoardDto(board.get());

        if (userSession == null ) {
            throw new TicketingException(ErrorCode.INVALID_LOGIN);
        } else {
            if (!b_uidx.equals(userSession.getIdx())) {
                throw new TicketingException(ErrorCode.INVALID_USER);
            } else {
                if (board.isPresent()) {
                    BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(bIdx, boardRequest.getTitle(), boardRequest.getContent(), new Timestamp(new Date().getTime()));

                    if (boardRequest.getTitle() != null) {
                        boardDto.setTitle(boardDto.getTitle());
                    }
                    if (boardDto.getContent() != null) {
                        boardDto.setContent(boardDto.getContent());
                    }

                    boardRepository.save(boardDto);
                }
                board.ifPresent(t -> {
                    BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(bIdx, boardRequest.getTitle(), boardRequest.getContent(), new Timestamp(new Date().getTime()));

                    if (boardRequest.getTitle() != null) {
                        t.setTitle(boardDto.getTitle());
                    }
                    if (boardDto.getContent() != null) {
                        t.setContent(boardDto.getContent());
                    }

                    boardRepository.save(t);
                });

                return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "게시물 " + SuccessMessage.SUCCESS_UPDATE.getMessage(), null);
            }
        }
        return null;
    }

    public CommonResponse deletePost(int bIdx) {

        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");

        Board board = boardRepository.findById(bIdx).get();

        if (board == null) {
            throw new TicketingException(ErrorCode.INVALID_BOARD);
        }

        Integer b_uidx =board.getUserIdx();

        if (userSession == null ) {
            throw new TicketingException(ErrorCode.INVALID_LOGIN);
        }
        else {
            if (!b_uidx.equals(userSession.getIdx())) {
                throw new TicketingException(ErrorCode.INVALID_USER);
            } else {
                boardRepository.deleteById(bIdx);
                return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "게시물 " + SuccessMessage.SUCCESS_DELETE.getMessage(), null);
            }
        }
    }
}
