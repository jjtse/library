package cub.book.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookAddRq {

	@Schema(description = "Book-ISBN號碼")
	@JsonProperty("BookIsbn")
	@NotBlank(message = "{bookIsbn.constraint}")
	private String bookIsbn;

	@Schema(description = "Book-語言")
	@JsonProperty("BookLanguage")
	@Pattern(regexp = "1|2|3", message = "{queryType.constraint}")
	private String bookLanguage;

	@Schema(description = "Book-書名")
	@JsonProperty("BookName")
	@NotBlank(message = "{bookName.constraint}")
	private String bookName;

	@Schema(description = "Book-作者")
	@JsonProperty("BookAuthor")
	private String bookAuthor;

	@Schema(description = "Book-出版社")
	@JsonProperty("BookPublisher")
	private String bookPublisher;

	@Schema(description = "Book-出版日期")
	@JsonProperty("BookPubDate")
	private LocalDate bookPubDate;

}
