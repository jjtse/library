package cub.book.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import cub.book.entity.LogEntity;

@ApplicationScoped
public class BookProducer {
		
	@Inject @Channel("librarylog-out")
    Emitter<LogEntity> emitterLog;
	
	public void sendLogToKafka(LogEntity logEntity) {
		emitterLog.send(logEntity).toCompletableFuture().join();
	}

}