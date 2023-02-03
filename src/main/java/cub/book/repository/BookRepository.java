package cub.book.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

import cub.book.Utils.LogUtils;
import cub.book.dto.BookDto;
import cub.book.dto.BookQueryRq;
import cub.book.entity.BookEntity;
import cub.book.mapper.BookMapper;
import cub.book.service.RedisService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class BookRepository implements PanacheRepository<BookEntity> {

	@Inject
	LogUtils logUtils;

	@Inject
	EntityManager entityManager;

	@Inject
	BookMapper bookMapper;

	@Inject
	RedisService redisService;

	@Transactional
	public void deleteByIsbn(String book_isbn) {
		delete("book_isbn", book_isbn);

	}

	public List<BookDto> bookQuery(@Valid BookQueryRq bookQueryRq) {

		List<BookDto> lsBookDto = new ArrayList<>();
		switch (bookQueryRq.getQueryType()) {

		case "1":
			if (bookQueryRq.getBookIsbn().isEmpty() || bookQueryRq.getBookIsbn() == null) {
				List<BookEntity> redisAllEntity = redisService.hgetAll("BookQueryCase1");
				if (redisAllEntity.size() != 0) {
					for (BookEntity bookEntity : redisAllEntity) {
						lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
					}
					logUtils.message("INFO", "bookQuery", "query was executed successfully by redis");
				} else {
					List<BookEntity> lsBookEntity = listAll();
					for (BookEntity bookEntity : lsBookEntity) {
						redisService.hsetAll("BookQueryCase1", bookEntity.getBookIsbn(), bookEntity);
						lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
					}
					logUtils.message("INFO", "bookQuery", "query was executed successfully by mysql");
				}

			} else {
				String key = bookQueryRq.getBookIsbn();
				if (redisService.exitstKey(key)) {
					BookEntity redis_data = redisService.getBookRq(key);
					try {
						Optional<BookEntity> optBookEntity = Optional.of(redis_data);
						if (optBookEntity.isPresent()) {
							BookEntity bookEntity = optBookEntity.get();
							lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
							logUtils.message("INFO", "bookQuery", "query was executed successfully by redis");
						}
					} catch (Exception e) {
						return lsBookDto;
					}
				} else {
					PanacheQuery<BookEntity> paBookEntity = find("bookIsbn", bookQueryRq.getBookIsbn());
					Optional<BookEntity> optBookEntity = paBookEntity.singleResultOptional();

					if (optBookEntity.isPresent()) {
						BookEntity bookEntity = optBookEntity.get();
						redisService.set(key, bookEntity);
						lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
						logUtils.message("INFO", "bookQuery", "query was executed successfully by mysql");
					}
				}
			}
			break;

		case "2":

			String QueryCase2key = "BookQueryCase2:" + bookQueryRq.getBookStatus();
			if (redisService.exitstKey(QueryCase2key)) {
				Set<BookEntity> setBookEntity = redisService.getBookQuery(QueryCase2key);

				for (BookEntity bookEntity : setBookEntity) {
					lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
				}
				logUtils.message("INFO", "bookQuery", "query was executed successfully by redis");

			} else {

				PanacheQuery<BookEntity> paBookEntity = find("bookStatus", bookQueryRq.getBookStatus());
				List<BookEntity> lsBookEntity = paBookEntity.list();

				for (BookEntity bookEntity : lsBookEntity) {
					lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
					redisService.setBookQuery(QueryCase2key, bookEntity);
				}
				logUtils.message("INFO", "bookQuery", "query was executed successfully by mysql");
			}

			break;

		case "3":

			String key = "BookQueryCase3:" + bookQueryRq.getBookName();

			if (redisService.exitstKey(key)) {

				Set<BookEntity> setBookEntity = redisService.getBookQuery(key);

				for (BookEntity bookEntity : setBookEntity) {
					lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
				}

				logUtils.message("INFO", "bookQuery", "query was executed successfully by redis");

			} else {

				List<BookEntity> lsBookEntity = entityManager
						.createQuery("select e from BookEntity e where e.bookName like ?1", BookEntity.class)
						.setParameter(1, "%" + bookQueryRq.getBookName() + "%").getResultList();

				for (BookEntity bookEntity : lsBookEntity) {
					lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
					redisService.setBookQuery(key, bookEntity);
				}

				logUtils.message("INFO", "bookQuery", "query was executed successfully by mysql");
			}
			break;
		}

		return lsBookDto;
	}
}