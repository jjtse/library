package cub.book.dto;

import javax.validation.constraints.NotBlank;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookInOutRq {

	@Schema(description="Book-ISBN號碼")
	@JsonProperty("BookIsbn")
	@NotBlank(message="{bookIsbn.required}")
	private String bookIsbn;
	
	
	@Schema(description="Book-借閱者")
	@JsonProperty("BookBorrowerId")
	@NotBlank(message="{bookBorrowerId.required}")
	private String bookBorrowerId;
	
}