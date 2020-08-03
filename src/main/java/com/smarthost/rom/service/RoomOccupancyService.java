package com.smarthost.rom.service;

import com.smarthost.rom.business.occupancy.RoomOccupancyManager;
import com.smarthost.rom.domain.RoomType;
import com.smarthost.rom.dto.RoomOccupancyResponse;
import com.smarthost.rom.entity.Customer;
import com.smarthost.rom.repository.UserPayWillingnessRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.smarthost.rom.domain.RoomType.ECONOMY;
import static com.smarthost.rom.domain.RoomType.PREMIUM;
import static com.smarthost.rom.exception.Messages.ROOMS_ONLY_POSITIVE_MSG;

@Service
public class RoomOccupancyService {

    private final UserPayWillingnessRepository repository;
    private final RoomOccupancyManager occupancyManager;

    public RoomOccupancyService(UserPayWillingnessRepository repository, RoomOccupancyManager occupancyManager) {
        this.repository = repository;
        this.occupancyManager = occupancyManager;
    }

    public RoomOccupancyResponse getOccupancy(int economy, int premium) {
        if (economy < 0 || premium < 0) {
            throw new IllegalArgumentException(ROOMS_ONLY_POSITIVE_MSG);
        }

        List<Customer> users = repository.getCustomers();
        List<Integer> customerSolvency = users.stream().map(Customer::getWishedPayment).collect(Collectors.toList());

        Map<RoomType, List<Integer>> allocatedCustomers =
                occupancyManager.allocateCustomers(customerSolvency, Map.of(ECONOMY, economy, PREMIUM, premium));
        Arrays.stream(RoomType.values())
                .filter(x -> x != RoomType.NONE)
                .forEach(x -> allocatedCustomers.putIfAbsent(x, Collections.emptyList()));

        var gain = allocatedCustomers.entrySet().stream()
                .filter(x -> !RoomType.NONE.equals(x.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .mapToInt(Integer::valueOf)
                .sum();

        return new RoomOccupancyResponse(allocatedCustomers.get(ECONOMY).size(),
                allocatedCustomers.get(PREMIUM).size(), gain);
    }
}
