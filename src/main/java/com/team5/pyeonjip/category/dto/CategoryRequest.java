package com.team5.pyeonjip.category.dto;

import lombok.*;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryRequest {

    private Long id;

    private String name;

    private int sort;

    private Long parentId;
}
