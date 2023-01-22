package cub.book.exception;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import cub.book.dto.base.CubResponse;
import cub.book.enums.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException e) {
		
		// errorId
		String errorId = UUID.randomUUID().toString();
		log.error("errorId[{}]", errorId, e);
		
		// errorMessage
		List<ErrorResponse.ErrorMessage> errorMessage = 
			e.getConstraintViolations()
			.stream()
			.map(exception -> new ErrorResponse.ErrorMessage(exception.getPropertyPath().toString(),exception.getMessage()))
			.collect(Collectors.toList());

		CubResponse<ErrorResponse> cubRs = new CubResponse<ErrorResponse>();
		cubRs.setReturnCodeAndDesc(ReturnCodeEnum.E001);
		cubRs.setTranRs(new ErrorResponse(errorId,errorMessage));
		
		
		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(cubRs).
				build();
	}

}

//https://developers.redhat.com/articles/2022/03/03/rest-api-error-modeling-quarkus-20#update_the_validation_messages
//https://stackoverflow.com/questions/60008540/quarkus-exception-handler
//https://stackoverflow.com/questions/55209807/throw-custom-exception-while-deserializing-the-date-field-using-jackson-in-java
//https://stackoverflow.com/questions/50571687/how-to-return-a-custom-response-pojo-when-request-body-fails-validations-that-ar

//https://blog.51cto.com/u_3664660/3215111
//https://blog.csdn.net/coolcoffee168/article/details/103503622

//https://openhome.cc/Gossip/Encoding/ResourceBundle.html
