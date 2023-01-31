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
import cub.book.dto.BookInOutRq;
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

		if (lsBookDto.size() != 0 && lsBookDto.get(0).getBookIsbn() != null) {
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
		BookEntity bookEntity = new BookEntity();
		bookEntity.setBookIsbn(bookUpdateRq.getBookIsbn());
		bookEntity.setBookLanguage(bookUpdateRq.getBookLanguage());
		bookEntity.setBookName(bookUpdateRq.getBookName());
		bookEntity.setBookAuthor(bookUpdateRq.getBookAuthor());
		bookEntity.setBookPublisher(bookUpdateRq.getBookPublisher());
		bookEntity.setBookPubDate(bookUpdateRq.getBookPubDate());
		bookEntity.setBookCreateDate(bookUpdateRq.getBookCreateDate());
		bookEntity.setBookStatus(bookUpdateRq.getBookStatus());
		redisService.set(key, bookEntity);

		CubResponse<BookUpdateRq> cubRs = new CubResponse<BookUpdateRq>();

		PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", bookUpdateRq.getBookIsbn());
		Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();
		if (optBookEntity.isPresent()) {
			BookEntity bookEntities = optBookEntity.get();
			bookMapper.BookUpdateRqToBookEntity(bookUpdateRq, bookEntities);
			bookRepository.persist(bookEntities);
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
			return cubRs;
		}
		cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
		return cubRs;
	}

	@Override
	@Transactional
	public CubResponse<BookInOutRq> bookBorrow(@Valid BookInOutRq bookInOutRq, String type) {
		CubResponse<BookInOutRq> cubRs = new CubResponse<BookInOutRq>();
		String key = bookInOutRq.getBookIsbn();
		if (redisService.getBookRq(key) != null) {
			BookEntity redisBookData = redisService.get(key);
			if ("2".equals(redisBookData.getBookStatus())) {
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，該書已借出");
			} else {
				BookEntity bookEntity = new BookEntity();
				bookEntity.setBookIsbn(redisBookData.getBookIsbn());
				bookEntity.setBookLanguage(redisBookData.getBookLanguage());
				bookEntity.setBookName(redisBookData.getBookName());
				bookEntity.setBookAuthor(redisBookData.getBookAuthor());
				bookEntity.setBookPublisher(redisBookData.getBookPublisher());
				bookEntity.setBookPubDate(redisBookData.getBookPubDate());
				bookEntity.setBookCreateDate(redisBookData.getBookCreateDate());
				bookEntity.setBookBorrowerId(bookInOutRq.getBookBorrowerId());
				bookEntity.setBookStatus("2");
				bookEntity.setBorrowDate(LocalDate.now());
				redisService.set(key, bookEntity);

				PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", bookInOutRq.getBookIsbn());
				Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();
				if (optBookEntity.isPresent()) {
					BookEntity bookEntities = optBookEntity.get();
					bookMapper.BookBorrowRqToBookEntity(bookInOutRq, bookEntities, type);
					bookRepository.persist(bookEntities);
					cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
				}
			}

		} else {
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
		}
		return cubRs;
	}

	@Override
	@Transactional
	public CubResponse<BookInOutRq> bookReturn(@Valid BookInOutRq bookInOutRq, String type) {
		CubResponse<BookInOutRq> cubRs = new CubResponse<BookInOutRq>();
		String key = bookInOutRq.getBookIsbn();
		if (redisService.getBookRq(key) != null) {
			BookEntity redisBookData = redisService.get(key);
			if ("1".equals(redisBookData.getBookStatus())) {
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，該書已歸還");
			} else {
				BookEntity bookEntity = new BookEntity();
				bookEntity.setBookIsbn(redisBookData.getBookIsbn());
				bookEntity.setBookLanguage(redisBookData.getBookLanguage());
				bookEntity.setBookName(redisBookData.getBookName());
				bookEntity.setBookAuthor(redisBookData.getBookAuthor());
				bookEntity.setBookPublisher(redisBookData.getBookPublisher());
				bookEntity.setBookPubDate(redisBookData.getBookPubDate());
				bookEntity.setBookCreateDate(redisBookData.getBookCreateDate());
				bookEntity.setBookBorrowerId("");
				bookEntity.setBookStatus("1");
				bookEntity.setBorrowDate(null);
				redisService.set(key, bookEntity);

				PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", bookInOutRq.getBookIsbn());
				Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();
				if (optBookEntity.isPresent()) {
					BookEntity bookEntities = optBookEntity.get();
					bookMapper.BookBorrowRqToBookEntity(bookInOutRq, bookEntities, type);
					bookRepository.persist(bookEntities);
					cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
				}
			}

		} else {
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
		}
		return cubRs;
	}

}
