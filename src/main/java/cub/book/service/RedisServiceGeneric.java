//package cub.book.service;
//
//import io.quarkus.redis.datasource.RedisDataSource;
//import io.quarkus.redis.datasource.value.ValueCommands;
//
//public class RedisServiceGeneric<T> {
//
//	private final ValueCommands<String,T> commands;
//	
//	public RedisServiceGeneric(RedisDataSource ds, Class<T> valueType) {
//        commands = ds.value(valueType);
//    }
//	
//	public T get(String key) {
//		return commands.get(key);
//	}
//	
//	public void set(String key, T valueType) {
//		commands.setex(key, 120, valueType);
//	}
//	
//}
