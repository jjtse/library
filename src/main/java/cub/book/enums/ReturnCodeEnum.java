package cub.book.enums;

public enum ReturnCodeEnum {
	
	SUCCESS("0000", "交易成功"),
	V001("V001", "缺乏必要輸入欄位"),
	E001("E001", "交易失敗"),
	E999("E999", "其他錯誤");

	private String returnCode;

	private String returnDesc;
	
	private ReturnCodeEnum(String returnCode, String returnDesc) {
		this.returnCode = returnCode;
		this.returnDesc = returnDesc;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public String getReturnCode() {
		return returnCode;
	}
}
