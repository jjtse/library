package cub.book.dto;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookQueryRs {

	@Schema(description="Book-結果數量")
	@JsonProperty("BookCount")
	private Integer bookCount;

	@Schema(description="Book-列表")
	@JsonProperty("BookList")
	private List<BookDto> bookList;

}
