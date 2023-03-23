package com.example.demo.Service;

import com.example.demo.Domain.BoardEntity;
import com.example.demo.Dto.BoardDto;
import com.example.demo.Respository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    private final int BLOCK_PAGE_NUM_COUNT = 5; //블럭에 존재하는 페이지 번호 수
    private final int PAGE_POST_COUNT = 4; // 한 페이지에 존재하는 게시글 수

    @Transactional
    public List<BoardDto> getBoardlist(Integer pageNum){
        Page<BoardEntity> page = boardRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC,"createdDate")));

        List<BoardEntity> boardEntities = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(boardEntity.EntityToDto());
        }
        return boardDtoList;
    }

    //총 게시글 수 반환 메서드
    @Transactional
    public Long getBoardCount(){
        return boardRepository.count();
    }

    public Integer[] getPageList(Integer curPageNum){
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        //총 게시글 수
        Double postsTotalPageNum = Double.valueOf(getBoardCount());

        // 총게시글 기준으로 계산한 마지막 페이지 번호
        Integer totalLastPageNum = (int)(Math.ceil(postsTotalPageNum/PAGE_POST_COUNT));

        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum+BLOCK_PAGE_NUM_COUNT)
                ? curPageNum+BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

        //페이지 시작 번호 조정
        curPageNum = (curPageNum<=3)?1: curPageNum -2;

        //페이지 번호 할당
        for(int val = curPageNum, idx=0; val<=blockLastPageNum; val++, idx++){
            pageList[idx]=val;
        }
        return pageList;
    }

//    @Transactional
//    public List<BoardDto> getBoardlist(){
//        List<BoardEntity> boardEntities = boardRepository.findAll();
//        List<BoardDto> boardDtoList = new ArrayList<>();
//        for (BoardEntity boardEntity : boardEntities) {
//            BoardDto boardDto = BoardDto.builder()
//                    .id(boardEntity.getId())
//                    .content(boardEntity.getContent())
//                    .writer(boardEntity.getWriter())
//                    .title(boardEntity.getTitle())
//                    .createdDate(boardEntity.getCreatedDate())
//                    .build();
//            boardDtoList.add(boardDto);
//        }
//        return boardDtoList;
//    }

    @Transactional
    public Long savePost(BoardDto boardDto){
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public BoardDto getPost(Long id) {
        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);
        BoardEntity boardEntity = boardEntityWrapper.get();

        BoardDto boardDto = BoardDto.builder()
                .id(boardEntity.getId())
                .writer(boardEntity.getWriter())
                .content(boardEntity.getContent())
                .title(boardEntity.getTitle())
                .createdDate(boardEntity.getCreatedDate())
                .build();

        return boardDto;
    }

    @Transactional
    public List<BoardDto> searchPost(String keyword) {
        List<BoardEntity> boardEntities = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if (boardEntities.isEmpty()){
            return boardDtoList;
        }
        for(BoardEntity boardEntity: boardEntities){
            BoardDto boardDto = boardEntity.EntityToDto();
            boardDtoList.add(boardDto);
        }

        return boardDtoList;
    }
}
