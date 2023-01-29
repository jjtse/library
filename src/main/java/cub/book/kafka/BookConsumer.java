//package cub.book.kafka;
//
//import javax.enterprise.context.ApplicationScoped;
//
//import org.eclipse.microprofile.reactive.messaging.Incoming;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@ApplicationScoped
//public class BookConsumer {
//	
////	@Inject
////	ObjectMapper objectMapper;
//	
////	  @Incoming("library-in")
////	    public void receive(BookQueryRq bookQueryRq) {
////	        log.info("Got a movie, title:{} year:{}", bookQueryRq.getBookStatus(), bookQueryRq.getBookIsbn());
////	        System.out.println("成功");
////	    }
//	  
////	  
////	  	@Incoming("library-in")
////	    @Outgoing("uppercase")
////	    public Message<String> toUpperCase(Message<String> message) {
////	        return message.withPayload(message.getPayload().toUpperCase());
////	    }
//
//	    /**
//	     * Consume the uppercase channel (in-memory) and print the messages.
//	     * @throws JsonProcessingException 
//	     * @throws JsonMappingException 
//	     **/
//	    @Incoming("library-in")
//	    public void sink(String word) throws JsonMappingException, JsonProcessingException {
////	    	BookQueryRq bookQueryRq = objectMapper.readValue(word,BookQueryRq.class);
////	        System.out.println(">> " + bookQueryRq.getBookIsbn());
//	        System.out.println(">> " + word);
//	    }
//}
