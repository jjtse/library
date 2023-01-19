package cub.book.dto;

import javax.validation.constraints.Pattern;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookQueryRq {
	
	@Schema(description="Book-ISBN號碼")
	@JsonProperty("BookIsbn")
	private String bookIsbn;
	
	@Schema(description="Book-狀態")
	@JsonProperty("BookStatus")
	private String bookStatus;
	
	@Schema(description="Book-書名")
	@JsonProperty("BookName")
	private String bookName;
	
	@Pattern(regexp="1|2|3",message="QueryType 只有 1、2、3 這三種")
	@Schema(description="Book-查詢種類")
	@JsonProperty("QueryType")
	private String queryType;
	
}

// https://blog.csdn.net/jx520/article/details/107894857
// @Range(min=1,max=3,message="QueryTyp只有三種")
// @Pattern(regexp="1|2|3",message="QueryTyp只有三種")
// 兩種都可以，但用正則表達式比較好，因為 swagger 會預設成你限制的正則表達式內容

// @JsonInclude(JsonInclude.Include.NON_NULL)
// 若希望 BookQueryRq 轉 JSON 時排除全部值為 null 的屬性，
// 可在 BookQueryRq 的類別名稱前加上 @JsonInclude(JsonInclude.Include.NON_NULL)
// 因此當 BookQueryRq 有些屬性為 null時，最後 JSON 可以只顯示 BookQueryRq 有值的屬性
