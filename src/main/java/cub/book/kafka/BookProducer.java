package cub.book.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import cub.book.dto.BookQueryRq;

@ApplicationScoped
public class BookProducer {
	
	 @Inject @Channel("library-out")
	    Emitter<BookQueryRq> emitter;

	    public void sendMessageToKafka(BookQueryRq book) {
	        emitter.send(book);
	    }
}
