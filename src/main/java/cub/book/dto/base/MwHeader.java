package cub.book.dto.base;

import javax.validation.constraints.NotBlank;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MwHeader {
	
	@Schema(description="交易代號")
	@JsonProperty("MSGID")
	@NotBlank(message="MSGID IS BLANK")
	private String msgId;
	
	@Schema(description="來源端APID")
	@JsonProperty("SOURCECHANNEL")
	@NotBlank(message="SOURCECHANNEL IS BLANK")
	private String sourceChannel;
	
	@Schema(description="處理結果代碼")
	@JsonProperty("RETURNCODE")
	private String returnCode;
	
	@Schema(description="處理結果訊息")
	@JsonProperty("RETURNDESC")
	private String returnDesc;
	
	@Schema(description="交易序號")
	@JsonProperty("TXNSEQ")
	@NotBlank(message="TXNSEQ IS BLANK")
	private String txnSeq;

	@Schema(description="中台交易序號")
	@JsonProperty("O360SEQ")	
	private String o360Seq;

}

// To ignore any unknown properties in JSON input without exception:
// @JsonIgnoreProperties(ignoreUnknown=true)

// @JsonInclude(JsonInclude.Include.NON_NULL)
// 若希望 MwHeader 轉 JSON 時排除全部值為 null 的屬性，
// 可在 MwHeader 的類別名稱前加上 @JsonInclude(JsonInclude.Include.NON_NULL)
// 因此當 MwHeader 有些屬性為 null時，最後 JSON 可以只顯示 MwHeader 有值的屬性

// schema 就是 Swagger 的 @ApiModelProperty
// https://juejin.cn/post/6870422762217799694
// https://stackoverflow.com/questions/72221934/apimodelproperty-to-schema

// @NotBlank
// validate msgId sourceChannel txnSeq
// 如果這三個屬性為空值，當 @Valid MwHeader，因為這三個屬性為空，就會報錯並顯示錯誤訊息
// https://quarkus.io/guides/validation


