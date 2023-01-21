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
public class BookUpdateRq {

	@Schema(description="Book-ISBN號碼")
	@JsonProperty("BookIsbn")
	@NotBlank(message="{bookIsbn.required}")
	private String bookIsbn;
	
	@Schema(description="Book-語言")
	@JsonProperty("BookLanguage")
	@Pattern(regexp="1|2|3",message="{bookLanguage.constraint}")
	private String bookLanguage;
	
	@Schema(description="Book-書名")
	@JsonProperty("BookName")
	@NotBlank(message="{bookName.required}")
	private String bookName;
	
	@Schema(description="Book-作者")
	@JsonProperty("BookAuthor")
	private String bookAuthor;
	
	@Schema(description="Book-出版社")
	@JsonProperty("BookPublisher")
	private String bookPublisher;
	
	@Schema(description="Book-出版日期")
	@JsonProperty("BookPubDate")
	private LocalDate bookPubDate;
	
	@Schema(description="Book-創建日期")
	@JsonProperty("BookCreateDate")
	private LocalDate bookCreateDate;
	
	@Schema(description="Book-狀態")
	@JsonProperty("BookStatus")
	@NotBlank(message="{bookStatus.required}")
	private String bookStatus;
	
}
