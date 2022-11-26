package com.ulalala.board_jpa.service;

import com.ulalala.board_jpa.common.response.CommonResponse;
import com.ulalala.board_jpa.common.response.ResponseStatus;
import com.ulalala.board_jpa.common.response.SuccessMessage;
import com.ulalala.board_jpa.common.response.exception.ErrorCode;
import com.ulalala.board_jpa.common.response.exception.TicketingException;
import com.ulalala.board_jpa.dao.repository.BoardRepository;
import com.ulalala.board_jpa.dao.repository.CommentRepository;
import com.ulalala.board_jpa.domain.Comment;
import com.ulalala.board_jpa.dto.comment.CommentDto;
import com.ulalala.board_jpa.dto.comment.CommentRequest;
import com.ulalala.board_jpa.dto.comment.CommentResponse;
import com.ulalala.board_jpa.dto.user.UserSession;
import com.ulalala.board_jpa.util.SessionUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CommentService {

    private final UserService userService;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public CommentService(UserService userService, BoardRepository boardRepository, CommentRepository commentRepository) {
        this.userService = userService;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    private CommentDto createCommentListDto(CommentResponse commentResponse) {
        List<CommentDto> commentLayer = new ArrayList<>();
        CommentDto curCommentDto =
                CommentDto.builder()
                        .commentNo(commentResponse.getCommentNo())
                        .boardNo(commentResponse.getBoardNo())
                        .content(commentResponse.getContent())
                        .userIdx(commentResponse.getUserIdx())
                        .userName(commentResponse.getUserName())
                        .date(commentResponse.getDate())
                        .parentId(commentResponse.getParentId())
                        .groupNo(commentResponse.getGroupNo())
                        .layer(commentResponse.getLayer())
                        .childCnt(commentResponse.getChildCnt())
                        .groupOrd(commentResponse.getGroupOrd())
                        .commentDtos(commentLayer)
                        .build();
        return curCommentDto;
    }

    public List<CommentDto> selectCommentsByPostId(Integer bIdx) {
//        List<CommentResponse> orderedComments = commentMapper.selectRecursiveComments(bIdx);
//        List<CommentDto> respList = new ArrayList<>();
//
//        for (CommentResponse comment : orderedComments) {
//            if (comment.getParentId() == 0) {
//                List<CommentDto> groupList = new ArrayList<>();
//                CommentDto rootComment = createCommentListDto(comment); // 최상위 댓글
//
//                for(CommentResponse c : orderedComments) {
//                    if (comment.getGroupNo() == c.getGroupNo()) {
//                        groupList.add(createCommentListDto(c));
//                    }
//                }
//
//                rootComment.setCommentDtos(recursiveComment(groupList, rootComment.getCommentNo(), rootComment.getLayer()+1, rootComment.getChildCnt()));
//                respList.add(rootComment);
//            }
//        }
//        return respList;
        return null;
    }

    private List<CommentDto> recursiveComment (List<CommentDto> groupList, Integer parentId, Integer layer) {

        List<CommentDto> childList = new ArrayList<>();
        for (CommentDto r : groupList) {
            if (Objects.equals(r.getLayer(), layer) && Objects.equals(r.getParentId(), parentId)) {
                childList.add(r);
            }
        }
        for (CommentDto r : childList) {
//            r.setCommentDtos(recursiveComment(groupList, r.getCommentNo(), layer+1, r.getChildCnt()));
        }
        return childList;
    }

    public CommonResponse post(Integer bIdx, CommentRequest commentRequest) {
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");

//        if (!boardRepository.findById(bIdx).isPresent()) { // optional, null
//            throw new TicketingException(ErrorCode.INVALID_BOARD);
//        }
//        else if (userSession == null) {
//            throw new TicketingException(ErrorCode.INVALID_LOGIN);
//        } else {
//            Comment comment = null;
//            if (commentRequest.getParentId() == null) {
//                comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), null, null, 0, 0, 0);
//                commentMapper.insertRootComment(comment);
//
//            } else {
//                if (commentMapper.selectCommentByCommentId(commentRequest.getParentId()) == null) {
//                    throw new TicketingException(ErrorCode.INVALID_COMMENT);
//                }
//                // 부모 댓글이 해당 게시글의 댓글이 아닐 경우
//                comment = new Comment(null, bIdx, commentRequest.getContent(), userSession.getIdx(), new Timestamp(new Date().getTime()), commentRequest.getParentId(), null, 0, 0, 0);
//                commentMapper.insertComment(comment);
//                commentMapper.updateParentChildCnt(comment.getParentId());
//
//            }
//
//            return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "댓글 " + SuccessMessage.SUCCESS_CREATE.getMessage(), null);
//        }
        return null;
    }

    // 댓글 삭제시 삭제된 댓글입니다로 변경
    public CommonResponse delete(Integer cIdx) {
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");
        Optional<Comment> comment = commentRepository.findById(cIdx);
        Integer c_uidx = comment.get().getUserIdx();

        if (c_uidx == null) throw new TicketingException(ErrorCode.INVALID_COMMENT);
        else {
            if (userSession == null) {
                throw new TicketingException(ErrorCode.INVALID_LOGIN);
            }
            else {
                if (!c_uidx.equals(userSession.getIdx())) {
                    throw new TicketingException(ErrorCode.INVALID_USER);
                } else {
                    commentRepository.deleteById(cIdx);
                    return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "댓글 " + SuccessMessage.SUCCESS_DELETE.getMessage(), null);
                }
            }
        }
    }
}
