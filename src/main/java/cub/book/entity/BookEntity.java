package cub.book.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="tb_book_info")
public class BookEntity {
	
	@Id
	@Column(name="book_isbn")	
	private String bookIsbn;
	
	@Column(name="book_language")	
	private String bookLanguage;
	
	@Column(name="book_name")
	private String bookName;
	
	@Column(name="book_author")
	private String bookAuthor;
	
	@Column(name="book_publisher")
	private String bookPublisher;
	
	@Column(name="book_pub_date")
	private LocalDate bookPubDate;
	
	@Column(name="book_create_date")
	private LocalDate bookCreateDate;
	
	@Column(name="book_status")
	private String bookStatus;
	
	@Column(name="book_borrower_id")
	private String bookBorrowerId;
	
	@Column(name="borrow_date")
	private LocalDate borrowDate;
	
}