package com.cedarmeadowmeats.orderservice.model;

import software.amazon.awssdk.annotations.NotNull;

public final class DJContactFormSubmissionRequest extends SubmissionRequest {

    @NotNull
    private final String eventDate;

    private final String venue;


    public DJContactFormSubmissionRequest(String name, String email, String phone, String comments, FormEnum form, OrganizationIdEnum organizationId, String eventDate, String venue, String hashKey) {
        super(name, email, phone, comments, form, organizationId, hashKey);
        this.eventDate = eventDate;
        this.venue = venue;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getVenue() {
        return venue;
    }
}
