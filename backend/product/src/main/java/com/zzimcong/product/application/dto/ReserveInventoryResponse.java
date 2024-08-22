package com.zzimcong.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReserveInventoryResponse {
    private boolean success;
    private String message;
}