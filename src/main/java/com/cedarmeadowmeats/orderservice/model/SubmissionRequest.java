package com.cedarmeadowmeats.orderservice.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import software.amazon.awssdk.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "form")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderFormSubmissionRequest.class, name = "order-form"),
})
public class SubmissionRequest {
    @NotNull
    protected final String name;
    @NotNull
    protected final String email;
    @NotNull
    protected final String phone;
    protected final String comments;
    protected final String form;
    protected final String organizationId;

    public SubmissionRequest(@NotNull String name, @NotNull String email, @NotNull String phone, String comments,
                             String form, String organizationId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.comments = comments;
        this.form = form;
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getComments() {
        return comments;
    }

    public String getForm() {
        return form;
    }

    public String getOrganizationId() {
        return organizationId;
    }

}
