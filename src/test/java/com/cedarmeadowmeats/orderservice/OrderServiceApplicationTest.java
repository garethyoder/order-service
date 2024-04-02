package com.cedarmeadowmeats.orderservice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderServiceApplicationTest {

    @Test
    public void testOrderServiceApplication() {
        OrderServiceApplication orderServiceApplication = new OrderServiceApplication();
        assertNotNull(orderServiceApplication);
    }

    @Test
    public void testOrderServiceApplicationCorsConfigurer() {
        OrderServiceApplication orderServiceApplication = new OrderServiceApplication();
        assertNotNull( orderServiceApplication.corsConfigurer());
    }
}