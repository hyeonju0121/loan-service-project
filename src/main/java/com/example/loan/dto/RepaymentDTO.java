package com.example.loan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RepaymentDTO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private BigDecimal repaymentAmount;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long applicationId;

        private BigDecimal repaymentAmount;

        private BigDecimal balance;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

    }
}
