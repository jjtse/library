package cub.book.service;

import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;

import cub.book.dto.BookQueryRq;
import cub.book.dto.BookUpdateRq;
//import io.quarkus.redis.datasource.ReactiveRedisDataSource;
//import io.vertx.mutiny.redis.client.RedisAPI;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;

@ApplicationScoped
public class RedisService {
	
    //@Inject ReactiveRedisDataSource reactiveDataSource;
    //@Inject RedisDataSource redisDataSource;
    //@Inject RedisAPI redisAPI;
	
	private final ValueCommands<String,BookUpdateRq> commandsBookUpdateRq;
	private final ValueCommands<String,BookQueryRq> commandsBookQueryRq;
	
	public RedisService(RedisDataSource ds) {
		commandsBookUpdateRq = ds.value(BookUpdateRq.class);
		commandsBookQueryRq= ds.value(BookQueryRq.class);
    }
	
	public BookUpdateRq get(String key) {
		return commandsBookUpdateRq.get(key);
	}
	
	public void set(String key, BookUpdateRq valueType) {
		commandsBookUpdateRq.setex(key, 300, valueType);
	}
	
	public BookQueryRq getBookQueryRq(String key) {
		return commandsBookQueryRq.get(key);
	}
	
	public void setBookQueryRq(String key, BookQueryRq valueType) {
		commandsBookQueryRq.setex(key, 300, valueType);
	}
}
