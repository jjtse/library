package cub.book.dto;

import java.time.LocalDate;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

	@Schema(description="Book-ISBN號碼")
	@JsonProperty("BookIsbn")
	private String bookIsbn;
	
	@Schema(description="Book-語言")
	@JsonProperty("BookLanguage")
	private String bookLanguage;
	
	@Schema(description="Book-書名")
	@JsonProperty("BookName")
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
	private String bookStatus;
	
	@Schema(description="Book-借閱者")
	@JsonProperty("BookBorrowerId")
	private String bookBorrowerId;
	
	@Schema(description="Book-借閱日期")
	@JsonProperty("BorrowerDate")
	private LocalDate borrowDate;
}
