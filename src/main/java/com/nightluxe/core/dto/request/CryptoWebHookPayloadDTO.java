package com.nightluxe.core.dto.request;

import lombok.Data;

@Data

public class CryptoWebHookPayloadDTO {
    private String paymentId; // Intern ID of payment processor
    private String paymentStatus; // ex: "waiting" "confirming" "finished"
    private String actuallyPaid; // how much was transaction
    private String orderId; // our ID for transaction sent for initialize

}
