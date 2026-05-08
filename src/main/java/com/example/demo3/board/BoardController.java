package com.example.demo3.board;

import com.example.demo3.category.Category;
import com.example.demo3.category.CategoryService;
import com.example.demo3.comment.CommentResponse;
import com.example.demo3.comment.CommentService;
import com.example.demo3.user.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;
    private final CategoryService categoryService;
    private final CommentService commentService;


    @GetMapping("/main")
    public String main(Model model, HttpServletRequest request) {
        List<BoardResponse.ListDTO> list = boardService.postList();
        log.info("boards 사이즈 : {}", list.size());
        model.addAttribute("boards", list);
        return "index";
    }



    // 메인 페이지 + 게시글 목록 요청
    // GET http://localhost:8080/
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        List<BoardResponse.ListDTO> boardList = boardService.postList();
        model.addAttribute("boards",boardList);
        return "index";
    }

    @GetMapping("/board/list")
    public String list(@RequestParam(required = false) String category, Model model) {
        List<BoardResponse.ListDTO> boardList;
        if (category != null && !category.isBlank()) {
            boardList = boardService.categories(category);
        } else {
            boardList = boardService.postList();
        }
        model.addAttribute("boardList", boardList);
        return "board/list";
    }

    // 게시글 작성 화면 요청
    // GET http://localhost:8080/board/save-form
    @GetMapping("/board/save-form")
    public String saveForm(Model model) {
        // 카테고리 목록 조회하고 뷰전달
        List<Category> categoryList = categoryService.categorylist();
        model.addAttribute("categories", categoryList);
        return "board/save-form";
    }

    // 게시글 작성 기능 요청
    // POST http://localhost:8080/board/save
    @PostMapping("/board/save")
    public String saveProc(BoardRequest.SaveDTO saveDTO, HttpSession session) {
        // 유효성 검사
        saveDTO.validate();
        UserResponse.SessionDTO sessionUser = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
        boardService.writePost(saveDTO, sessionUser);
        return "redirect:/main";
    }

    // 게시글 상세 화면 요청
    // GET http://localhost:8080/board/{id}
    @GetMapping("/board/{id}")
    public String detailPage(@PathVariable Integer id, Model model, HttpSession session) {
        UserResponse.SessionDTO sessionUser = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
        Integer sessionUserId = sessionUser != null ? sessionUser.getId() : null;
        BoardResponse.DetailDTO detailDTO = boardService.postDetails(id);
        boolean isOwner = sessionUser != null && sessionUser.getId().equals(detailDTO.getUserId());
        List<CommentResponse.DetailDTO> comments = commentService.getCommentsByBoardId(id, sessionUserId);
        model.addAttribute("board", detailDTO);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("comments", comments);
        return "board/detail";
    }

    // 게시글 삭제 기능 요청
    // POST http://localhost:8080/board/{id}/delete
    @PostMapping("/board/{id}/delete")
    public String deleteProc(@PathVariable Integer id, HttpSession session) {
        UserResponse.SessionDTO sessionUser = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
        boardService.deletePost(id,sessionUser);
        return "redirect:/";
    }

    // 게시글 수정 화면 요청
    // GET http://localhost:8080/board/{id}/update-form
    @GetMapping("/board/{id}/update-form")
    public String updateFormPage(@PathVariable Integer id, Model model) {
        BoardResponse.DetailDTO detailDTO = boardService.postDetails(id);
        List<Category> categories = categoryService.categorylist();
        model.addAttribute("board", detailDTO);
        model.addAttribute("categories", categories);
        return "board/update-form";

    }

    // 게시글 수정 기능 요청
    // http://localhost:8080/board/{id}/update
    @PostMapping("/board/{id}/update")
    public String updateProc(@PathVariable Integer id, BoardRequest.UpdateDTO updateDTO, HttpSession session) {
        updateDTO.validate();
       UserResponse.SessionDTO sessionUser = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
       boardService.updatePost(id,updateDTO,sessionUser);
       return "redirect:/board/" + id;
    }


}
