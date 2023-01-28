package cub.book.service;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;

import cub.book.dto.BookDeleteRq;
import cub.book.dto.BookQueryRq;
import cub.book.dto.BookUpdateRq;
import cub.book.entity.BookEntity;
//import io.quarkus.redis.datasource.ReactiveRedisDataSource;
//import io.vertx.mutiny.redis.client.RedisAPI;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;

@ApplicationScoped
public class RedisService {

	// @Inject ReactiveRedisDataSource reactiveDataSource;
	// @Inject RedisDataSource redisDataSource;
	// @Inject RedisAPI redisAPI;

	private final ValueCommands<String, BookUpdateRq> commandsBookUpdateRq;
	private final ValueCommands<String, BookQueryRq> commandsBookQueryRq;
	private final ValueCommands<String, BookDeleteRq> commandsBookDeleteRq;
	private final ValueCommands<String, BookEntity> commandsBookAddRq;
	private final ValueCommands<String, BookEntity> commandsBookAllRq;
	private KeyCommands<String> keys;

	public RedisService(RedisDataSource ds) {
		commandsBookUpdateRq = ds.value(BookUpdateRq.class);
		commandsBookQueryRq = ds.value(BookQueryRq.class);
		commandsBookDeleteRq = ds.value(BookDeleteRq.class);
		commandsBookAddRq = ds.value(BookEntity.class);
		commandsBookAllRq = ds.value(BookEntity.class);
		keys = ds.key();
	}

	public BookUpdateRq get(String key) {
		return commandsBookUpdateRq.get(key);
	}

	public void set(String key, BookUpdateRq valueType) {
		commandsBookUpdateRq.set(key, valueType);
	}

	public BookQueryRq getBookQueryRq(String key) {
		return commandsBookQueryRq.get(key);
	}

	public void setBookQueryRq(String key, BookQueryRq valueType) {
		commandsBookQueryRq.set(key, valueType);
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
		return commandsBookAllRq.mget(key);
	}

}
