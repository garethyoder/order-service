package com.cedarmeadowmeats.orderservice.model;

import software.amazon.awssdk.annotations.NotNull;

public final class DJContactFormSubmissionRequest extends SubmissionRequest {

    @NotNull
    private final String eventDate;

    private final String venue;


    public DJContactFormSubmissionRequest(String name, String email, String phone, String comments, FormEnum form, OrganizationIdEnum organizationId, String eventDate, String venue) {
        super(name, email, phone, comments, form, organizationId);
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
