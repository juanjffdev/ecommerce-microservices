package com.solenia.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Cart {

    private int id;

    private int userId;

    private OrderStatus status;

    private LocalDateTime createdAt;
}
