package cub.book.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

import cub.book.dto.BookAddRq;
import cub.book.dto.BookDeleteRq;
import cub.book.dto.BookDto;
import cub.book.dto.BookQueryRq;
import cub.book.dto.BookQueryRs;
import cub.book.dto.BookUpdateRq;
import cub.book.dto.base.CubResponse;
import cub.book.entity.BookEntity;
import cub.book.enums.ReturnCodeEnum;
import cub.book.mapper.BookMapper;
import cub.book.repository.BookRepository;
import cub.book.service.BookService;
import cub.book.service.RedisService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@ApplicationScoped
public class BookServiceImpl implements BookService {

	@Inject
	RedisService redisService;

	@Inject
	BookMapper bookMapper;

	private BookRepository bookRepository;

	@Inject
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	@Transactional
	public CubResponse<BookAddRq> insertBookData(@Valid BookAddRq bookAddRq) {
		String key = bookAddRq.getBookIsbn();
		CubResponse<BookAddRq> cubRs = new CubResponse<BookAddRq>();

		if (redisService.getBookRq(key) == null) {
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
			redisService.setBookAddRq(key, bookEntity);
			bookRepository.persist(bookEntity);
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
			return cubRs;
		} else {
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料已存在");
			return cubRs;
		}
	}

	@Override
	public CubResponse<BookDeleteRq> deleteBookData(@Valid BookDeleteRq bookDeleteRq) {
		CubResponse<BookDeleteRq> cubRs = new CubResponse<BookDeleteRq>();
		String key = bookDeleteRq.getBookIsbn();
		PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", bookDeleteRq.getBookIsbn());
		Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();

		if (optBookEntity.isPresent()) {
			bookRepository.deleteByIsbn(bookDeleteRq.getBookIsbn());
			redisService.deleteBookDeleteRq(key);
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
			return cubRs;
		}
		cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
		return cubRs;

	}

	@Override
	public CubResponse<BookQueryRs> bookQuery(@Valid BookQueryRq bookQueryRq) {
		List<BookDto> lsBookDto = bookRepository.bookQuery(bookQueryRq);
		BookQueryRs bookQueryRs = new BookQueryRs();

		if (lsBookDto.size() != 0) {
			bookQueryRs.setBookCount(lsBookDto.size());
			bookQueryRs.setBookList(lsBookDto);
			CubResponse<BookQueryRs> cubRs = new CubResponse<BookQueryRs>();
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
			cubRs.setTranRs(bookQueryRs);
			return cubRs;
		} else {
			CubResponse<BookQueryRs> cubRs = new CubResponse<BookQueryRs>();
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
			return cubRs;
		}
	}

	@Override
	@Transactional
	public CubResponse<BookUpdateRq> bookUpdate(@Valid BookUpdateRq bookUpdateRq) {
		String key = bookUpdateRq.getBookIsbn();
		redisService.set(key, bookUpdateRq);
		System.out.println(redisService.get(key).getBookIsbn());
		System.out.println(redisService.get(key).getBookName());
		System.out.println(redisService.get(key).getBookLanguage());
		System.out.println(redisService.get(key).getBookAuthor());
		System.out.println(redisService.get(key).getBookPublisher());
		System.out.println(redisService.get(key).getBookPubDate());
		System.out.println(redisService.get(key).getBookCreateDate());
		System.out.println(redisService.get(key).getBookStatus());

		CubResponse<BookUpdateRq> cubRs = new CubResponse<BookUpdateRq>();

		PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", bookUpdateRq.getBookIsbn());
		Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();
		if (optBookEntity.isPresent()) {
			BookEntity bookEntity = optBookEntity.get();
			bookMapper.BookUpdateRqToBookEntity(bookUpdateRq, bookEntity);
			bookRepository.persist(bookEntity);
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
			return cubRs;
		}
		cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
		return cubRs;
	}

}
