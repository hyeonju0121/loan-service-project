package com.example.loan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultType {
    SUCCESS("0000", "success"),

    SYSTEM_ERROR("9000", "system error"),

    NOT_FOUND_APPLICATION("9000", "대출 신청 내역이 존재하지 않습니다.");

    private final String code;
    private final String desc;
}
