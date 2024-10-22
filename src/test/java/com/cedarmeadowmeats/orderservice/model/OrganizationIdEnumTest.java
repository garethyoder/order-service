package com.cedarmeadowmeats.orderservice.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class OrganizationIdEnumTest {

    @Test
    void testGetCompanyName() {
        Assertions.assertEquals("Cedar Meadow Meats", OrganizationIdEnum.getCompanyName(OrganizationIdEnum.CEDAR_MEADOW_MEATS));
        Assertions.assertEquals("Cedar Meadow Naturals", OrganizationIdEnum.getCompanyName(OrganizationIdEnum.CEDAR_MEADOW_NATURALS));
        Assertions.assertEquals("G Yoder Audio Expressions", OrganizationIdEnum.getCompanyName(OrganizationIdEnum.G_YODER_AUDIO_EXPRESSIONS));
    }

    @Test
    void testValue() {
        Assertions.assertEquals("cedarmeadowmeats", OrganizationIdEnum.CEDAR_MEADOW_MEATS.getValue());
    }
}