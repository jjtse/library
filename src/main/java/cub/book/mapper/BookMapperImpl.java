package cub.book.mapper;

import javax.enterprise.context.ApplicationScoped;

import cub.book.dto.BookDto;
import cub.book.dto.BookUpdateRq;
import cub.book.entity.BookEntity;

@ApplicationScoped
public class BookMapperImpl implements BookMapper {

	@Override
	public BookDto BookEntityToBookDto(BookEntity bookEntity) {
		BookDto bookDto = new BookDto();
		bookDto.setBookIsbn(bookEntity.getBookIsbn());
		bookDto.setBookLanguage(bookEntity.getBookLanguage());
		bookDto.setBookName(bookEntity.getBookName());
		bookDto.setBookAuthor(bookEntity.getBookAuthor());
		bookDto.setBookPublisher(bookEntity.getBookPublisher());
		bookDto.setBookPubDate(bookEntity.getBookPubDate());
		bookDto.setBookCreateDate(bookEntity.getBookCreateDate());
		bookDto.setBookStatus(bookEntity.getBookStatus());
		bookDto.setBookBorrowerId(bookEntity.getBookBorrowerId());
		bookDto.setBorrowDate(bookEntity.getBorrowDate());
		return bookDto;
	}

	@Override
	public BookEntity BookUpdateRqToBookEntity(BookUpdateRq bookUpdateRq,BookEntity bookEntity) {
		bookEntity.setBookIsbn(bookUpdateRq.getBookIsbn());
		bookEntity.setBookLanguage(bookUpdateRq.getBookLanguage());
		bookEntity.setBookName(bookUpdateRq.getBookName());
		bookEntity.setBookAuthor(bookUpdateRq.getBookAuthor());
		bookEntity.setBookPublisher(bookUpdateRq.getBookPublisher());
		bookEntity.setBookPubDate(bookUpdateRq.getBookPubDate());
		bookEntity.setBookCreateDate(bookUpdateRq.getBookCreateDate());
		bookEntity.setBookStatus(bookUpdateRq.getBookStatus());
		return bookEntity;
	}

	
}
