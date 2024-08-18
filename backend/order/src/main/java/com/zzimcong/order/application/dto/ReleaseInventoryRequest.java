package com.zzimcong.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReleaseInventoryRequest {
    private int quantity;
}