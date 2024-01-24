package com.cedarmeadowmeats.orderservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import software.amazon.awssdk.annotations.NotNull;

public final class OrderFormSubmissionRequest extends SubmissionRequest {

    @NotNull
    @JsonProperty("selection")
    private final OrderFormSelectionEnum orderFormSelectionEnum;

    public OrderFormSubmissionRequest(String name, String email, String phone, String comments, FormEnum form, OrganizationIdEnum organizationId, OrderFormSelectionEnum orderFormSelectionEnum) {
        super(name, email, phone, comments, form, organizationId);
        this.orderFormSelectionEnum = orderFormSelectionEnum;
    }


    @NotNull
    public OrderFormSelectionEnum orderFormSelectionEnum() {
        return orderFormSelectionEnum;
    }

    public OrderFormSelectionEnum getOrderFormSelectionEnum() {
        return orderFormSelectionEnum;
    }
}
