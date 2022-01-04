package com.luv2code.springdemo.service;

import com.luv2code.springdemo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Service
public class CustomerServiceRestClientImpl implements CustomerService {

    private final RestTemplate restTemplate;

    private final String url;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public CustomerServiceRestClientImpl(RestTemplate restTemplate,
            @Value("${crm.rest.url}") String url) {
        this.url = url;
        this.restTemplate = restTemplate;

        logger.info("Loaded property: crm.rest.url="
                + url);
    }

    @Override
    public List<Customer> getCustomers() {
        logger.info("in getCustomers(): Calling REST API " + url);

        // make a REST call
        ResponseEntity<List<Customer>> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }
        );

        // get the list of customers from response
        List<Customer> customers = responseEntity.getBody();
        logger.info("in getCustomers(): customers " + customers);

        return customers;
    }

    @Override
    public void saveCustomer(Customer theCustomer) {
        int id = theCustomer.getId();

        // make a rest call
        if (id == 0) {
            // add employee
            restTemplate.postForEntity(url, theCustomer, Customer.class);
        }
        else {
            restTemplate.put(url, theCustomer);
        }
    }

    @Override
    public Customer getCustomer(int theId) {
        return restTemplate.getForObject(url + "/" + theId, Customer.class);
    }

    @Override
    public void deleteCustomer(int theId) {
        restTemplate.delete(url + "/" + theId);
    }
}
