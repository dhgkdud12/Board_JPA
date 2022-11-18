package com.ulalala.board_jpa.dto.board;

import com.ulalala.board_jpa.domain.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class BoardSearchRequest extends Paging {
    private String searchType;
    private String keyword;

    public BoardSearchRequest(String searchType, String keyword, int curPage, int pageSize, int blockSize, int totalCnt) {
        super(curPage, pageSize, blockSize, totalCnt);
        this.searchType = searchType;
        this.keyword = keyword;
    }
}
