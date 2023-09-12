package com.example.loan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultType {
    SUCCESS("0000", "success"),

    SYSTEM_ERROR("9000", "system error"),

    NOT_FOUND_APPLICATION("9000", "대출 신청 내역이 존재하지 않습니다."),

    NOT_FOUND_JUDGMENT("9000", "대출 심사 내역이 존재하지 않습니다."),

    NOT_ACCEPT_ALL_TERMS("9000", "모든 약관에 동의하지 않았습니다."),

    ACCEPT_NOT_EXIST_TERMS("9000", "존재하지 않는 약관에 동의했습니다."),

    NOT_APPROVED("9000", "아직 대출 승인이 되지 않았습니다."),

    ALREADY_EXIST_CONTRACT("9000", "이미 계약이 체결되었습니다."),

    NOT_FOUND_CONTRACT("9000", "아직 계약이 체결되지 않았습니다."),

    ALREADY_EXIST_BALANCE("9000", "해당 신청 건은 이미 집행되었습니다."),

    NOT_EXIST_FILE("4001", "파일이 존재하지 않습니다.");

    private final String code;
    private final String desc;
}
