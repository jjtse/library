package cub.book.mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import cub.book.dto.BookDto;
import cub.book.dto.BookQueryRq;
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
	public BookDto BookEntityToBookDto(BookEntity bookEntity, BookQueryRq bookQueryRq) {
		String key = bookQueryRq.getBookIsbn();
		BookEntity redis_data = redisService.getBookRq(key);
		BookDto bookDto = new BookDto();
		bookDto.setBookIsbn(redis_data.getBookIsbn());
		bookDto.setBookLanguage(redis_data.getBookLanguage());
		bookDto.setBookName(redis_data.getBookName());
		bookDto.setBookAuthor(redis_data.getBookAuthor());
		bookDto.setBookPublisher(redis_data.getBookPublisher());
		bookDto.setBookPubDate(redis_data.getBookPubDate());
		bookDto.setBookCreateDate(redis_data.getBookCreateDate());
		bookDto.setBookStatus(redis_data.getBookStatus());
		bookDto.setBookBorrowerId(redis_data.getBookBorrowerId());
		bookDto.setBorrowDate(redis_data.getBorrowDate());
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

}
