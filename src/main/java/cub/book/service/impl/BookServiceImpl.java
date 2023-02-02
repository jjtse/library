package cub.book.service.impl;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

import cub.book.Utils.LogUtils;
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

	@Inject
	LogUtils logUtils;

	private BookRepository bookRepository;

	@Inject
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	@Transactional
	public CubResponse<BookAddRq> insertBookData(@Valid BookAddRq bookAddRq) {

		CubResponse<BookAddRq> cubRs = new CubResponse<BookAddRq>();
		PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", bookAddRq.getBookIsbn());
		Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();

		try {
			if (!optBookEntity.isPresent()) {
				bookRepository.persist(bookMapper.BookAddRqToBookEntity(bookAddRq));
				logUtils.message("INFO", "bookAdd", "mysql was created successful");
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
				logUtils.message("INFO", "bookAdd", "response data: " + cubRs.toString());
				redisService.deleteKeysAll();
				logUtils.message("INFO", "bookAdd", "redis was flushed successful");
			} else {
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料已存在");
				logUtils.message("INFO", "bookAdd", "response data: " + cubRs.toString());
			}
		} catch (Exception e) {
			logUtils.message("ERROR", "bookAdd", "Error message: " + e.getMessage());
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E999);
			return cubRs;
		}
		return cubRs;
	}

	@Override
	public CubResponse<BookDeleteRq> deleteBookData(@Valid BookDeleteRq bookDeleteRq) {
		CubResponse<BookDeleteRq> cubRs = new CubResponse<BookDeleteRq>();
		PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", bookDeleteRq.getBookIsbn());
		Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();

		try {
			if (optBookEntity.isPresent()) {
				bookRepository.deleteByIsbn(bookDeleteRq.getBookIsbn());
				logUtils.message("INFO", "deleteAdd", "sql was deleted successful");
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
				logUtils.message("INFO", "deleteAdd", "response data: " + cubRs.toString());
				redisService.deleteKeysAll();
				logUtils.message("INFO", "deleteAll", "redis was flushed successful");
				return cubRs;
			}
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
			logUtils.message("INFO", "deleteAdd", "response data: " + cubRs.toString());
			return cubRs;
		} catch (Exception e) {
			logUtils.message("ERROR", "bookAdd", "Error message: " + e.getMessage());
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E999);
			return cubRs;
		}
	}

	@Override
	public CubResponse<BookQueryRs> bookQuery(@Valid BookQueryRq bookQueryRq) {
		List<BookDto> lsBookDto = bookRepository.bookQuery(bookQueryRq);
		BookQueryRs bookQueryRs = new BookQueryRs();
		CubResponse<BookQueryRs> cubRs = new CubResponse<BookQueryRs>();

		try {
			if (lsBookDto.size() != 0 && lsBookDto.get(0).getBookIsbn() != null) {
				bookQueryRs.setBookCount(lsBookDto.size());
				bookQueryRs.setBookList(lsBookDto);
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
				cubRs.setTranRs(bookQueryRs);
				if ("1".equals(bookQueryRq.getQueryType())) {
					logUtils.message("INFO", "bookQuery", "bookQuery all has successful");
				} else {
					logUtils.message("INFO", "bookQuery", "response data: " + cubRs.toString());
				}
			} else {
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
				logUtils.message("INFO", "bookQuery", "response data: " + cubRs.toString());
			}
		} catch (Exception e) {
			logUtils.message("ERROR", "bookQuery", "Error message: " + e.getMessage());
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E999);
			return cubRs;
		}
		return cubRs;
	}

	@Override
	@Transactional
	public CubResponse<BookUpdateRq> bookUpdate(@Valid BookUpdateRq bookUpdateRq) {
		CubResponse<BookUpdateRq> cubRs = new CubResponse<BookUpdateRq>();
		PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", bookUpdateRq.getBookIsbn());
		Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();

		try {
			if (optBookEntity.isPresent()) {
				BookEntity bookEntities = optBookEntity.get();
				bookMapper.BookUpdateRqToBookEntity(bookUpdateRq, bookEntities);
				bookRepository.persist(bookEntities);
				logUtils.message("INFO", "bookUpadte", "mysql was update successful");
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
				logUtils.message("INFO", "bookUpadte", "response data: " + cubRs.toString());
				redisService.deleteKeysAll();
				logUtils.message("INFO", "bookUpadte", "redis was flushed successful");
				return cubRs;
			}
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
			logUtils.message("INFO", "bookUpadte", "response data: " + cubRs.toString());
			return cubRs;

		} catch (Exception e) {
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E999);
			logUtils.message("ERROR", "bookUpadte", "Error message: " + e.getMessage());
			return cubRs;
		}
	}

	@Override
	@Transactional
	public CubResponse<BookInOutRq> bookBorrow(@Valid BookInOutRq bookInOutRq) {
		CubResponse<BookInOutRq> cubRs = new CubResponse<BookInOutRq>();
		String key = bookInOutRq.getBookIsbn();
		try {
			PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", key);
			Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();
			if (optBookEntity.isPresent()) {
				BookEntity bookData = optBookEntity.get();
				if ("2".equals(bookData.getBookStatus())) {
					cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，該書已借出");
					logUtils.message("INFO", "bookBorrow", "mysql check bookBorrow Status was borrowed");
				} else {
					BookEntity bookEntity = optBookEntity.get();
					bookEntity.setBookStatus("2");
					bookRepository.persist(bookEntity);
					logUtils.message("INFO", "bookBorrow", "mysql update bookBorrow Status was successful");
					cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
					logUtils.message("INFO", "bookBorrow", "response data: " + cubRs.toString());
					redisService.deleteKeysAll();
					logUtils.message("INFO", "bookBorrow", "redis was flushed successful");
				}
			} else {
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
				logUtils.message("INFO", "bookBorrow", "response data: " + cubRs.toString());
			}

		} catch (Exception e) {
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E999);
			logUtils.message("ERROR", "bookBorrow", "Error message: " + e.getMessage());
			return cubRs;
		}

		return cubRs;
	}

	@Override
	@Transactional
	public CubResponse<BookInOutRq> bookReturn(@Valid BookInOutRq bookInOutRq) {
		CubResponse<BookInOutRq> cubRs = new CubResponse<BookInOutRq>();
		String key = bookInOutRq.getBookIsbn();
		try {
			PanacheQuery<BookEntity> paBookEntity = bookRepository.find("bookIsbn", key);
			Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();
			if (optBookEntity.isPresent()) {
				BookEntity bookData = optBookEntity.get();
				if ("1".equals(bookData.getBookStatus())) {
					cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，該書已歸還");
					logUtils.message("INFO", "bookReturn", "mysql check bookReturn Status has Returned already");
				} else {
					BookEntity bookEntity = optBookEntity.get();
					bookEntity.setBookStatus("1");
					bookRepository.persist(bookEntity);
					logUtils.message("INFO", "bookReturn", "mysql update bookReturn Status was successful");
					cubRs.setReturnCodeAndDesc(ReturnCodeEnum.SUCCESS);
					logUtils.message("INFO", "bookReturn", "response data: " + cubRs.toString());
					redisService.deleteKeysAll();
					logUtils.message("INFO", "bookReturn", "redis was flushed successful");
				}

			} else {
				cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001, "，資料不存在");
				logUtils.message("INFO", "bookReturn", "response data: " + cubRs.toString());
			}
		} catch (Exception e) {
			cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E999);
			logUtils.message("ERROR", "bookReturn", "Error message: " + e.getMessage());
			return cubRs;
		}
		return cubRs;
	}


}
