package cub.book.controller;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import cub.book.dto.BookAddRq;
import cub.book.dto.BookDeleteRq;
import cub.book.dto.BookDto;
import cub.book.dto.BookQueryRq;
import cub.book.dto.BookQueryRs;
import cub.book.dto.BookUpdateRq;
import cub.book.dto.base.CubResponse;
import cub.book.kafka.BookProducer;
import cub.book.service.BookService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@Path("")
public class BookController {

	private BookService bookService;
	
//	private static final Logger logger = LogManager.getLogger(BookController.class);
	
	@Inject
	BookProducer booskProducer;

	@Inject
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@Operation(summary = "新增書籍")
	@POST
	@Path("/book/add")
	public RestResponse<CubResponse<BookAddRq>> bookAdd(BookDto bookdto) {
		CubResponse<BookAddRq> cubRs = bookService.insertBookData(bookdto);
		LocalDateTime currentTime = LocalDateTime.now();
		return ResponseBuilder.ok(cubRs, MediaType.APPLICATION_JSON).header("date", currentTime).build();
	}

	@Operation(summary = "刪除書籍")
	@POST
	@Path("/book/delete")
	public RestResponse<CubResponse<BookDeleteRq>> bookDelete(BookDeleteRq bookDeleteRq) {
		CubResponse<BookDeleteRq> cubRs = bookService.deleteBookData(bookDeleteRq);
		LocalDateTime currentTime = LocalDateTime.now();
		return ResponseBuilder.ok(cubRs, MediaType.APPLICATION_JSON).header("date", currentTime).build();
	}

	@Operation(summary = "書籍查詢")
	@POST
	@Path("/book/query")
	public RestResponse<CubResponse<BookQueryRs>> bookQuery(@Valid BookQueryRq bookQueryRq) {
		booskProducer.sendMessageToKafka(bookQueryRq);
//		logger.info("test");
//		log.info("BookQueryRq: {}", bookQueryRq);
//		log.info("query book");
		CubResponse<BookQueryRs> cubRs = bookService.bookQuery(bookQueryRq);
		LocalDateTime currentTime = LocalDateTime.now();
		return ResponseBuilder.ok(cubRs, MediaType.APPLICATION_JSON).header("date", currentTime).build();

	}

	@Operation(summary = "書籍修改")
	@POST
	@Path("/book/modify")
	public RestResponse<CubResponse<BookUpdateRq>> bookUpdate(@Valid BookUpdateRq bookUpdateRq) {
		CubResponse<BookUpdateRq> cubRs = bookService.bookUpdate(bookUpdateRq);
		LocalDateTime currentTime = LocalDateTime.now();
		return ResponseBuilder.ok(cubRs, MediaType.APPLICATION_JSON).header("date", currentTime).build();
	}
}

// https://developer.salesforce.com/docs/atlas.en-us.apexref.meta/apexref/apex_methods_system_restresponse.htm
// https://stackoverflow.com/questions/74646006/theres-a-alternative-for-responseentity-in-quarkus
// https://quarkus.io/guides/resteasy-reactive#resource-types
// https://quarkus.io/guides/rest-json

// @Valid CubRequest<BookQueryRq> cubRq
// 雖然說 CubRequest<BookQueryRq> 有寫 valid 了
// 但 controller 還要再寫 valid 才可以做驗證

// Swagger-UI 小陷阱
// The value / is not allowed as it blocks the application from serving anything else. A value prefixed with '/' makes it absolute and not relative.
// 所以 controller 前面要加上 @Path("")
// https://quarkus.io/guides/openapi-swaggerui
