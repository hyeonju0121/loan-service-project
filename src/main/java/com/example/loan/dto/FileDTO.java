package com.example.loan.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO implements Serializable {
    private String name;

    private String url;
}
