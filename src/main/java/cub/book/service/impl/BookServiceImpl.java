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

	// 1. 注入 redis 服務
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
		CubResponse<BookAddRq> cubRs = new CubResponse<BookAddRq>();
		// insert book_info
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
		bookRepository.persist(bookEntity);
		System.out.println("新增成功 ");
		cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
		return cubRs;
	}

	@Override
	public CubResponse<BookDeleteRq> deleteBookData(@Valid BookDeleteRq bookDeleteRq) {
		CubResponse<BookDeleteRq> cubRs = new CubResponse<BookDeleteRq>();
		String key = bookDeleteRq.getBookIsbn();
		// delete book_info by bookIsbn
		PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", bookDeleteRq.getBookIsbn());
		Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();
		if (optBookEntity.isPresent()) {
			bookRepository.deleteByIsbn(bookDeleteRq.getBookIsbn());
			redisService.deleteBookDeleteRq(key);
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
			return cubRs;
		}
		cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001,"，資料不存在");
		return cubRs;

	}

	@Override
	public CubResponse<BookQueryRs> bookQuery(@Valid BookQueryRq bookQueryRq) {

		// 2. 設定 redis key、取出 redis 值
		String key = "BookQuery" + bookQueryRq.getBookIsbn();
		redisService.setBookQueryRq(key, bookQueryRq);
		System.out.println(redisService.getBookQueryRq(key).getBookIsbn());
		System.out.println(redisService.getBookQueryRq(key).getBookName());

		List<BookDto> lsBookDto = bookRepository.bookQuery(bookQueryRq);
		BookQueryRs bookQueryRs = new BookQueryRs();
		bookQueryRs.setBookCount(lsBookDto.size());
		bookQueryRs.setBookList(lsBookDto);
		CubResponse<BookQueryRs> cubRs = new CubResponse<BookQueryRs>();
		cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
		cubRs.setTranRs(bookQueryRs);

		return cubRs;
	}

	@Override
	@Transactional
	public CubResponse<BookUpdateRq> bookUpdate(@Valid BookUpdateRq bookUpdateRq) {

		// 2. 設定 redis key、取出 redis 值
		String key = "BookUpdate" + bookUpdateRq.getBookIsbn();
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
		cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001,"，資料不存在");
		return cubRs;
	}

}
