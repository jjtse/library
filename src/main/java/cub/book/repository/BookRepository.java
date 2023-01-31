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
		String[] keys = redisService.keys().toString().replace("[", "").replace("]", "").split(", ");
		Map<String, BookEntity> RedisBookValue = redisService.getAllBookRq(keys);
		switch (bookQueryRq.getQueryType()) {

		case "1":
			if (bookQueryRq.getBookIsbn().isEmpty() || bookQueryRq.getBookIsbn() == null) {
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
			break;

		case "2":
			for (BookEntity bookEntity : RedisBookValue.values()) {
				if ("1".equals(bookQueryRq.getBookStatus()) && "1".equals(bookEntity.getBookStatus())
						&& bookEntity.getBookIsbn() != null) {
					lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
				} else if ("2".equals(bookQueryRq.getBookStatus()) && "2".equals(bookEntity.getBookStatus())
						&& bookEntity.getBookIsbn() != null) {
					lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
				} else if ("3".equals(bookQueryRq.getBookStatus()) && "3".equals(bookEntity.getBookStatus())
						&& bookEntity.getBookIsbn() != null) {
					lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
				}
			}
			break;

		case "3":
			for (BookEntity bookEntity : RedisBookValue.values()) {
				String EntityBookName = bookEntity.getBookName().toUpperCase();
				String RqBookName = bookQueryRq.getBookName().toUpperCase();
				if (EntityBookName.contains(RqBookName) && bookEntity.getBookIsbn() != null) {
					lsBookDto.add(bookMapper.AllBookEntityToBookDto(bookEntity));
				}
			}
			break;
		}
		return lsBookDto;
	}
}
