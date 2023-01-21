package cub.book.exception;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

	// 如果沒有 errorId 可以不用序列化 errorId 成為 JSON
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String errorId;

	private List<ErrorMessage> errorMessage;

	public ErrorResponse(String errorId, ErrorMessage errorMessage) {
		this.errorId = errorId;
		this.errorMessage = List.of(errorMessage);
	}

	public ErrorResponse(ErrorMessage errorMessage) {
		this.errorId = null;
		this.errorMessage = List.of(errorMessage);
	}
	
	public ErrorResponse(String errorId, List<ErrorMessage> errorMessage) {
		this.errorId = errorId;
		this.errorMessage = errorMessage;
	}

	public ErrorResponse(List<ErrorMessage> errorMessage) {
		this.errorId = null;
		this.errorMessage = errorMessage;
	}

	@Getter
	@Setter
	public static class ErrorMessage {

		// 如果沒有 path 可以不用序列化 path 成為 JSON
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private String path;
		
		private String message;

		public ErrorMessage(String path, String message) {
			this.path = path;
			this.message = message;
		}

		public ErrorMessage(String message) {
			this.path = null;
			this.message = message;
		}

	}

}

//https://developers.redhat.com/articles/2022/03/03/rest-api-error-modeling-quarkus-20#update_the_validation_messages
//https://stackoverflow.com/questions/60008540/quarkus-exception-handler
//https://stackoverflow.com/questions/55209807/throw-custom-exception-while-deserializing-the-date-field-using-jackson-in-java
//https://stackoverflow.com/questions/50571687/how-to-return-a-custom-response-pojo-when-request-body-fails-validations-that-ar

//https://blog.51cto.com/u_3664660/3215111
//https://blog.csdn.net/coolcoffee168/article/details/103503622

//https://openhome.cc/Gossip/Encoding/ResourceBundle.html
