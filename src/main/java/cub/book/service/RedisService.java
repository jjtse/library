package cub.book.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;

import cub.book.dto.BookDeleteRq;
import cub.book.entity.BookEntity;
//import io.quarkus.redis.datasource.ReactiveRedisDataSource;
//import io.vertx.mutiny.redis.client.RedisAPI;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.set.SetCommands;
import io.quarkus.redis.datasource.value.ValueCommands;

@ApplicationScoped
public class RedisService {

	// @Inject ReactiveRedisDataSource reactiveDataSource;
	// @Inject RedisDataSource redisDataSource;
	// @Inject RedisAPI redisAPI;

	private final ValueCommands<String, BookEntity> commandsBookUpdateRq;
	private final ValueCommands<String, BookDeleteRq> commandsBookDeleteRq;
	private final ValueCommands<String, BookEntity> commandsBookAddRq;
	private final ValueCommands<String, BookEntity> commandsBookAllRq;
	private final SetCommands<String, BookEntity> commandsBookQuery;
	private HashCommands<String, String, BookEntity> commandsBookAll;
	private KeyCommands<String> keys;

	public RedisService(RedisDataSource ds) {
		commandsBookUpdateRq = ds.value(BookEntity.class);
		commandsBookDeleteRq = ds.value(BookDeleteRq.class);
		commandsBookAddRq = ds.value(BookEntity.class);
		commandsBookAllRq = ds.value(BookEntity.class);
		commandsBookQuery = ds.set(BookEntity.class);
		commandsBookAll = ds.hash(BookEntity.class);
		keys = ds.key();
	}
	
	public void hsetAll(String key, String isbn, BookEntity bookEntity) {
		commandsBookAll.hset(key, isbn, bookEntity);
	}
	
	public List<BookEntity> hgetAll(String key) {
		return commandsBookAll.hvals(key);
	}

	public BookEntity get(String key) {
		return commandsBookUpdateRq.get(key);
	}

	public void set(String key, BookEntity valueType) {
		commandsBookUpdateRq.set(key, valueType);
	}

	public void deleteBookDeleteRq(String key) {
		commandsBookDeleteRq.getdel(key);
	}

	public boolean setBookAddRq(String key, BookEntity valueType) {
		return commandsBookAddRq.setnx(key, valueType);
	}

	public BookEntity getBookRq(String key) {
		return commandsBookAddRq.get(key);
	}

	public List<String> keys() {
		return keys.keys("*");
	}

	public Map<String, BookEntity> getAllBookRq(String[] key) {
		if (key[0].isEmpty()) {
			BookEntity bookEntity = new BookEntity();
			bookEntity.setBookIsbn(null);
			Map<String, BookEntity> checkMap = new HashMap<String, BookEntity>();
			Map.entry("check", bookEntity);
			return checkMap;
		} else {
			return commandsBookAllRq.mget(key);
		}
	}
	
	public void deleteKeysAll() {
		List<String> listKeys = keys.keys("*");
		String[] arrayKeys = listKeys.toString().replace("[", "").replace("]", "").split(", ");
		keys.del(arrayKeys);
	}
	
	public boolean exitstKey(String key) {
		return keys.exists(key);
	}
	
	public void setBookQuery(String key, BookEntity valueType) {
		commandsBookQuery.sadd(key, valueType);
	}
	
	public Set<BookEntity> getBookQuery(String key) {
		return commandsBookQuery.smembers(key);
	}
		
}
