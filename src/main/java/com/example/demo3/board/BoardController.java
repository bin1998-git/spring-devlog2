package com.example.demo3.board;

import com.example.demo3.category.Category;
import com.example.demo3.category.CategoryService;
import com.example.demo3.user.UserResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;
    private final CategoryService categoryService;


    // 메인 페이지 + 게시글 목록 요청
    // GET http://localhost:8080/
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        List<BoardResponse.ListDTO> boardList = boardService.postList();
        model.addAttribute("boards",boardList);
        return "index";
    }

    // 게시글 목록 화면 요청
    // GET http://localhost:8080/board/list
    @GetMapping("/board/list")
    public String list(Model model) {
        List<BoardResponse.ListDTO> boardList = boardService.postList();
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
        return "redirect:/";
    }

    // 게시글 상세 화면 요청
    // GET http://localhost:8080/board/{id}
    @GetMapping("/board/{id}")
    public String detailPage(@PathVariable Integer id, Model model, HttpSession session) {
        UserResponse.SessionDTO sessionUser = (UserResponse.SessionDTO) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO detailDTO = boardService.postDetails(id);
        boolean isOwner = sessionUser != null && sessionUser.getId().equals(detailDTO.getUserId());
        model.addAttribute("board", detailDTO);
        model.addAttribute("isOwner", isOwner);
        return "board/detail"; // board/detail.mustache 렌더링
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
