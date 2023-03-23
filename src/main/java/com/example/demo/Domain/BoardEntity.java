package com.example.demo.Domain;

import com.example.demo.Dto.BoardDto;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
//notnull이 작동이 안됨.
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "board")
public class BoardEntity extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    @NotNull
    private String writer;

    @Column(length = 10)
    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    public BoardDto EntityToDto(){
        BoardDto boardDto = BoardDto.builder()
                .id(id)
                .writer(writer)
                .title(title)
                .content(content)
                .createdDate(getCreatedDate())
                .build();
        return boardDto;
    }

    @Builder
    public BoardEntity(Long id, String writer, String title, String content){
        this.id=id;
        this.writer=writer;
        this.title=title;
        this.content=content;
    }
}
