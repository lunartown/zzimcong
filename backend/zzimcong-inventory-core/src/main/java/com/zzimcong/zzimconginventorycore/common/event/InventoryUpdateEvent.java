package com.zzimcong.zzimconginventorycore.common.event;

import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdateEvent implements KafkaMessage {
    private Long productId;
    private int quantity;
}
