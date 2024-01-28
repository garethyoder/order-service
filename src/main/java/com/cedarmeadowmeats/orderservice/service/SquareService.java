package com.cedarmeadowmeats.orderservice.service;

import com.squareup.square.SquareClient;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SquareService {

    private final SquareClient squareClient;

    public SquareService(final SquareClient squareClient) {
        this.squareClient = squareClient;
    }

    public List<Customer> getCustomersByEmail(final String email) {
        List<Customer> customers;
        try {
            SearchCustomersRequest request = new SearchCustomersRequest.Builder()
                    .query(new CustomerQuery.Builder()
                            .filter(new CustomerFilter.Builder()
                                    .emailAddress(new CustomerTextFilter.Builder()
                                            .exact(email)
                                            .build())
                                    .build())
                            .build())
                    .build();
            customers = squareClient.getCustomersApi().searchCustomers(request).getCustomers();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return customers;
    }
}
