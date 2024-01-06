package com.cedarmeadowmeats.orderservice.model;

import java.time.ZonedDateTime;

public class Submission {

    private String name;
    private String email;
    private String phone;
    private String form;
    private ZonedDateTime createdDate;
    private ZonedDateTime lastUpdatedDate;
    private Integer version;

    public Submission(final SubmissionRequest submissionRequest) {
        this.name = submissionRequest.name();
        this.email = submissionRequest.email();
        this.phone = submissionRequest.phone();
        this.form = submissionRequest.form();
        this.createdDate = ZonedDateTime.now();
        this.lastUpdatedDate = ZonedDateTime.now();
        this.version = 0;
    }

    public Submission() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
