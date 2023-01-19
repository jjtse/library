package cub.book.service;

import javax.validation.Valid;

import cub.book.dto.BookAddRq;
import cub.book.dto.BookDeleteRq;
import cub.book.dto.BookDto;
import cub.book.dto.BookQueryRq;
import cub.book.dto.BookQueryRs;
import cub.book.dto.BookUpdateRq;
import cub.book.dto.base.CubResponse;

public interface BookService {

	CubResponse<BookQueryRs> bookQuery(@Valid BookQueryRq bookQueryRq);

	CubResponse<BookUpdateRq> bookUpdate(@Valid BookUpdateRq bookUpdateRq);

	CubResponse<BookAddRq> insertBookData(BookDto bookdto);

	CubResponse<BookDeleteRq> deleteBookData(BookDeleteRq bookDeleteRq);

}
