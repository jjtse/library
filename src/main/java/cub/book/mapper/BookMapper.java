package cub.book.mapper;

import cub.book.dto.BookDto;
import cub.book.dto.BookUpdateRq;
import cub.book.entity.BookEntity;

public interface BookMapper {
	
	BookDto BookEntityToBookDto(BookEntity bookEntity);
	
	BookEntity BookUpdateRqToBookEntity(BookUpdateRq bookUpdateRq,BookEntity bookEntity);

}

// map construct 方案一直編譯失敗
// 其實 map construct 沒什麼特別的
// 就是透過這個依賴來自動建立一個 MapperImpl 裡面就是 BookEntityToBookDto 的 getter/setter
// 就如同 MapStruct(entity优雅的转dto) 這篇文章中的圖片一樣
// https://blog.csdn.net/m0_49496327/article/details/123694006
// 所以我們也可以自己寫
