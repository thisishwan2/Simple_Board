package com.example.demo.Controller;

import com.example.demo.Dto.BoardDto;
import com.example.demo.Respository.BoardRepository;
import com.example.demo.Service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Getter
public class BoardController {
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    // 게시물 목록
    @GetMapping("/")
    public String list(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum){
        List<BoardDto> boardList = boardService.getBoardlist(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "board/list.html";
    }

    // 글쓰기
    @GetMapping("/post")
    public String write(){
        return "board/write.html";
    }

    // 저장
    @PostMapping("/post")
    public String write(BoardDto boardDto){
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    //단건 조회
    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id")Long id, Model model){
        BoardDto boardDto = boardService.getPost(id);

        model.addAttribute("boardDto", boardDto);
        return "board/detail.html";
    }

    //단건 조회 후 수정 버튼 클릭
    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        BoardDto boardDto = boardService.getPost(id);

        model.addAttribute("boardDto", boardDto);
        return "board/update.html";
    }

    //pathvariable 왜 안써도 될까?
    // 업데이트
    @PutMapping("/post/edit/{id}")
    public String update(BoardDto boardDto){
        boardService.savePost(boardDto);

        return "redirect:/";
    }
    // 삭제
    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no")Long no){
        boardRepository.deleteById(no);
        return "redirect:/";
    }

    @GetMapping("/board/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model){
        List<BoardDto> boardDtoList = boardService.searchPost(keyword);

        model.addAttribute("boardList", boardDtoList);
        return "board/list.html";
    }
}


