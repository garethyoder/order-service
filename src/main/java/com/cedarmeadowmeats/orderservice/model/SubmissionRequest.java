package com.cedarmeadowmeats.orderservice.model;

import jakarta.validation.constraints.NotNull;

public class SubmissionRequest {
    @NotNull
    protected final String name;
    @NotNull
    protected final String email;
    @NotNull
    protected final String phone;
    protected final String comments;

    protected final FormEnum form;
    protected final OrganizationIdEnum organizationId;

    public SubmissionRequest(@NotNull String name, @NotNull String email, @NotNull String phone, String comments,
                             FormEnum form, OrganizationIdEnum organizationId) {
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

    public FormEnum getForm() {
        return form;
    }

    public OrganizationIdEnum getOrganizationId() {
        return organizationId;
    }

}
