package com.example.demo3.comment;

import com.example.demo3.user.UserResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 기능 요청
    // 주소설계 : http://localhost:8080/comment/save
    @PostMapping("/comment/save")
    public String saveProc(CommentRequest.SaveDTO saveDTO, HttpSession session) {

        // 유효성 검사
        saveDTO.validate();
       UserResponse.SessionDTO sessionUser = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
       commentService.comment(saveDTO,sessionUser);
       return "redirect:/board/" + saveDTO.getBoardId();
    }

    // 댓글 삭제 기능 요청
    // http://localhost:8080/comment/{id}/delete
    @PostMapping("/comment/{id}/delete")
    public String deleteProc(@PathVariable Integer id, @RequestParam Integer boardId, HttpSession session){
       UserResponse.SessionDTO sessionUser = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
       commentService.deleteComment(id, sessionUser);
       return "redirect:/board/" + boardId;
    }

}
