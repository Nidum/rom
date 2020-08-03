package com.smarthost.rom.business.occupancy;

import com.smarthost.rom.domain.RoomType;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.smarthost.rom.domain.RoomType.ECONOMY;
import static com.smarthost.rom.domain.RoomType.PREMIUM;
import static com.smarthost.rom.exception.Messages.CUSTOMERS_LIST_NOT_NULL;
import static com.smarthost.rom.exception.Messages.ROOMS_ONLY_POSITIVE_MSG;

@Component
public class RoomOccupancyManager {

    /**
     * Based on passed list of customers payment willingness and available rooms map, optimally allocates customers
     * in these rooms.
     *
     * @return map of room type to its allocated customers.
     */
    public Map<RoomType, List<Integer>> allocateCustomers(List<Integer> customerSolvency, Map<RoomType, Integer> roomCountMap) {
        if (customerSolvency == null) {
            throw new IllegalArgumentException(CUSTOMERS_LIST_NOT_NULL);
        }

        if (roomCountMap.values().stream().anyMatch(x -> x < 0) ||
                !Arrays.stream(RoomType.values())
                        .filter(x -> x != RoomType.NONE)
                        .allMatch(roomCountMap::containsKey)) {
            throw new IllegalArgumentException(ROOMS_ONLY_POSITIVE_MSG);
        }

        var customerSolvencyCopy = new ArrayList<>(customerSolvency);
        customerSolvencyCopy.sort(Comparator.naturalOrder());

        var result = new HashMap<RoomType, List<Integer>>();
        for (RoomType value : RoomType.values()) {
            result.put(value, new ArrayList<>());
        }

        var chain = new PremiumQualifier(roomCountMap, result.get(PREMIUM),
                new EconomyToPremiumUpgradeQualifier(roomCountMap, result.get(PREMIUM), customerSolvencyCopy,
                        new EconomyQualifier(roomCountMap, result.get(ECONOMY), null)));

        for (int i = customerSolvencyCopy.size() - 1; i >= 0; i--) {
            Integer customer = customerSolvencyCopy.get(i);
            result.get(chain.customerRoomType(customer)).add(customer);
        }

        return result;
    }
}
