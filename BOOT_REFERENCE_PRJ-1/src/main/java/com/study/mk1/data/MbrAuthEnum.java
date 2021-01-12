package com.study.mk1.data;

public enum MbrAuthEnum {
	
	YES("Y"), NO("N");
	
	
	public enum mbrGrdCd {
		
		GNRL, //일반
		CHAMP; //최고
	}
	
	public enum mbrStatCd {
		
		ACT, //사용중
		SCSI; //탈퇴
	}
	
	public enum mbrTpCd {
		
		GNRL, //일반
		ADMIN; //관리자
	}

	private final String text;

	private MbrAuthEnum(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
	
}
