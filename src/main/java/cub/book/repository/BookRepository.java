package cub.book.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

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
				String[] keys = redisService.keys().toString().replace("[", "").replace("]", "").split(", ");
				Map<String, BookEntity> RedisBookValue = redisService.getAllBookRq(keys);
				for (BookEntity bookEntity : RedisBookValue.values()) {
					lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
				}
			} else {
				String key = bookQueryRq.getBookIsbn();
				BookEntity redis_data = redisService.getBookRq(key);
				try {
					Optional<BookEntity> optBookEntity = Optional.of(redis_data);
					if (optBookEntity.isPresent()) {
						BookEntity bookEntity = optBookEntity.get();
						lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
					}
				} catch (Exception e) {
					return lsBookDto;
				}

			}
		case "2":
			if ("1".equals(bookQueryRq.getBookStatus())) {
				PanacheQuery<BookEntity> paBookEntity = find("bookStatus", bookQueryRq.getBookStatus());
				List<BookEntity> lsBookEntity = paBookEntity.list();
				for (BookEntity bookEntity : lsBookEntity) {
					lsBookDto.add(bookMapper.BookEntityToBookDto(bookEntity, bookQueryRq));
				}
			} else if ("2".equals(bookQueryRq.getBookStatus())) {
				PanacheQuery<BookEntity> paBookEntity = find("bookStatus", bookQueryRq.getBookStatus());
				List<BookEntity> lsBookEntity = paBookEntity.list();
				for (BookEntity bookEntity : lsBookEntity) {
					lsBookDto.add(bookMapper.BookEntityToBookDto(bookEntity, bookQueryRq));
				}
			} else if ("3".equals(bookQueryRq.getBookStatus())) {
				PanacheQuery<BookEntity> paBookEntity = find("bookStatus", bookQueryRq.getBookStatus());
				List<BookEntity> lsBookEntity = paBookEntity.list();
				for (BookEntity bookEntity : lsBookEntity) {
					lsBookDto.add(bookMapper.BookEntityToBookDto(bookEntity, bookQueryRq));
				}
			}
		case "3":
			List<BookEntity> lsBookEntity = entityManager
					.createQuery("select e from BookEntity e where e.bookName like ?1", BookEntity.class)
					.setParameter(1, "%" + bookQueryRq.getBookName() + "%").getResultList();
			for (BookEntity bookEntity : lsBookEntity) {
				lsBookDto.add(bookMapper.BookEntityToBookDto(bookEntity, bookQueryRq));
			}
		}
		return lsBookDto;
	}
}
