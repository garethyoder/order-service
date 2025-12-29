package com.cedarmeadowmeats.orderservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import software.amazon.awssdk.annotations.NotNull;

public final class OrderFormSubmissionRequest extends SubmissionRequest {

    @NotNull
    @JsonProperty("selection")
    private final OrderFormSelectionEnum orderFormSelectionEnum;

    @Nullable
    private final String referral;

    public OrderFormSubmissionRequest(String name, String email, String phone, String comments, FormEnum form, OrganizationIdEnum organizationId, OrderFormSelectionEnum orderFormSelectionEnum, @Nullable String referral, String hashKey) {
        super(name, email, phone, comments, form, organizationId, hashKey);
        this.orderFormSelectionEnum = orderFormSelectionEnum;
        this.referral = referral;
    }


    @NotNull
    public OrderFormSelectionEnum orderFormSelectionEnum() {
        return orderFormSelectionEnum;
    }

    public OrderFormSelectionEnum getOrderFormSelectionEnum() {
        return orderFormSelectionEnum;
    }

    @Nullable
    public String getReferral() {
        return referral;
    }
}
