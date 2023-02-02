package cub.book.service;

import javax.validation.Valid;

import cub.book.dto.BookAddRq;
import cub.book.dto.BookDeleteRq;
import cub.book.dto.BookInOutRq;
import cub.book.dto.BookQueryRq;
import cub.book.dto.BookQueryRs;
import cub.book.dto.BookUpdateRq;
import cub.book.dto.base.CubResponse;

public interface BookService {

	CubResponse<BookQueryRs> bookQuery(@Valid BookQueryRq bookQueryRq);

	CubResponse<BookUpdateRq> bookUpdate(@Valid BookUpdateRq bookUpdateRq);

	CubResponse<BookAddRq> insertBookData(@Valid BookAddRq bookAddRq);

	CubResponse<BookDeleteRq> deleteBookData(@Valid BookDeleteRq bookDeleteRq);

	CubResponse<BookInOutRq> bookBorrow(@Valid BookInOutRq bookInOutRq);

	CubResponse<BookInOutRq> bookReturn(@Valid BookInOutRq bookInOutRq);
}
