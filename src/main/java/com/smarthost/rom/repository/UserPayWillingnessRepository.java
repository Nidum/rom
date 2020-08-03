package com.smarthost.rom.repository;

import com.smarthost.rom.entity.Customer;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserPayWillingnessRepository {

    @NotNull List<Customer> getCustomers();

}
