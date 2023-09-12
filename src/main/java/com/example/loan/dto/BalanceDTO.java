package com.example.loan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BalanceDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private Long applicationId;

        private BigDecimal entryAmount;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {

        private Long applicationId;

        private BigDecimal beforeEntryAmount;

        private BigDecimal afterEntryAmount;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long balanceId;

        private Long applicationId;

        private BigDecimal balance;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }
}
