
package com.ini.board.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ini.board.vo.BoardDTO;

@Mapper
public interface BoardMapper {

    List<BoardDTO> selectBoardList(
        @Param("keyword") String keyword,
        @Param("category") String category,
        @Param("startRow") int startRow,
        @Param("pageSize") int pageSize
    );

    int countBoardList(
        @Param("keyword") String keyword,
        @Param("category") String category
    );
}
