package cub.book.dto.base;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import cub.book.enums.ReturnCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CubResponse<T> {
	
	@Schema(description="中台通用Json Header")
	@JsonProperty("MWHEADER")
	private MwHeader mwHeader;
	
	@Schema(description="中台通用Json Response")
	@JsonProperty("TRANRS")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T tranRs;
	
	public void setReturnCodeAndDesc(ReturnCodeEnum returnCodeEnum) {
		mwHeader = new MwHeader();
		this.mwHeader.setReturnCode(returnCodeEnum.getReturnCode());
		this.mwHeader.setReturnDesc(returnCodeEnum.getReturnDesc());
	}

}

// 因為 Response 不一定要有 MwHeader，所以不用 @Valid
// 因為 Response 即使有 MwHeader， 裡面屬性 msgId sourceChannel txnSeq 也可以為空(如只顯示returnCode、returnDesc)
// 因此 MwHeader，不用 @Valid
// 因為 Response 不一定要有 T 即使有 T 裡面屬性也可以為空，因此不用 @Valid

// @JsonInclude(JsonInclude.Include.NON_NULL)
// 若希望 T 轉 JSON 時排除全部值為 null 的屬性，
// 可在 T 的類別名稱前加上 @JsonInclude(JsonInclude.Include.NON_NULL)
// 因此當 T 有些屬性為 null時，最後 JSON 可以只顯示 T 有值的屬性