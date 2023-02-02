package cub.book.mapper;

import java.time.LocalDate;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import cub.book.dto.BookAddRq;
import cub.book.dto.BookDto;
import cub.book.dto.BookUpdateRq;
import cub.book.entity.BookEntity;
import cub.book.service.RedisService;

@ApplicationScoped
public class BookMapperImpl implements BookMapper {
	@Inject
	RedisService redisService;

	@Override
	public BookDto AllBookEntityToBookDto(BookEntity bookEntity) {

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
	public BookEntity BookUpdateRqToBookEntity(BookUpdateRq bookUpdateRq, BookEntity bookEntity) {
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

	@Override
	public BookEntity BookAddRqToBookEntity(BookAddRq bookAddRq) {
		LocalDate currentDate = LocalDate.now();
		BookEntity bookEntity = new BookEntity();
		bookEntity.setBookIsbn(bookAddRq.getBookIsbn());
		bookEntity.setBookLanguage(bookAddRq.getBookLanguage());
		bookEntity.setBookName(bookAddRq.getBookName());
		bookEntity.setBookAuthor(bookAddRq.getBookAuthor());
		bookEntity.setBookPublisher(bookAddRq.getBookPublisher());
		bookEntity.setBookStatus("1");
		bookEntity.setBookPubDate(bookAddRq.getBookPubDate());
		bookEntity.setBookCreateDate(currentDate);
		return bookEntity;
	}
	
	

}
