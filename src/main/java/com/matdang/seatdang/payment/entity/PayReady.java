package com.matdang.seatdang.payment.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PayReady {
    // TODO :  Persistable 구현
    @Id
    private String partnerOrderId;
    private String tid;
    private String partnerUserId;
    private Long shopId;
}
