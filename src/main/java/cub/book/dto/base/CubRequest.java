package cub.book.dto.base;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CubRequest<T> {
	
	@Schema(description="中台通用 Json Header")
	@JsonProperty("MWHEADER")
	@NotNull(message="MWHEADER IS NULL")
	@Valid
	private MwHeader mwHeader;
	
	@Schema(description="中台各自 JSON Request")
	@JsonProperty("TRANRQ")
	@NotNull(message="TRANRQ IS NULL")
	@Valid
	private T tranRq;
}

// @Valid MwHeader，MwHeader內有驗證規則，msgId sourceChannel txnSeq不能為空，如果為空就會報錯並顯示錯誤訊息
// @Valid T， T 內看有無驗證規則，如果不符合 T 驗證規則就會報錯並顯示錯誤訊息

// 檢查 CubRequest<T> 中兩個屬性 MwHeader T 是否為空，當 @Valid CubRequest<T>，如果這兩個屬性為空，就會報錯並顯示錯誤訊息
