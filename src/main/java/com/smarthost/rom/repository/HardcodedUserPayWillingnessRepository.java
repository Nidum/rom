package com.smarthost.rom.repository;

import com.smarthost.rom.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class HardcodedUserPayWillingnessRepository implements UserPayWillingnessRepository {

    public List<Customer> getCustomers() {
        return Stream.of(23, 45, 155, 374, 22, 99, 100, 101, 115, 209, 600)
                .map(Customer::new)
                .collect(Collectors.toList());
    }

}
