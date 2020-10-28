package com.smarthost.rom.business.occupancy;

import com.smarthost.rom.domain.RoomType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smarthost.rom.domain.RoomType.ECONOMY;
import static com.smarthost.rom.domain.RoomType.PREMIUM;

class EconomyToPremiumUpgradeQualifier extends CustomerTypeQualifier {

    private final List<Integer> allCustomers;
    private final List<Integer> premiumCustomers;

    public EconomyToPremiumUpgradeQualifier(Map<RoomType, Integer> roomCountMap, List<Integer> premiumCustomers,
                                            List<Integer> allCustomers, CustomerTypeQualifier next) {
        super(roomCountMap, next);
        this.premiumCustomers = premiumCustomers;
        this.allCustomers = new ArrayList<>(allCustomers);
    }

    @Override
    protected RoomType getType() {
        return RoomType.PREMIUM;
    }

    @Override
    protected boolean customerMatchesRoom(int wishedPayment) {
        int indexOf = allCustomers.lastIndexOf(wishedPayment);
        // All premium used
        boolean result =
                // Wished payment is not premium
                wishedPayment < getType().getLowerBoundary()
                        // Some premium rooms still left
                        && roomCountMap.get(PREMIUM) >= premiumCustomers.size() + 1
                        // All economy rooms will be occupied by others, who'll pay less
                        && roomCountMap.get(ECONOMY) <= indexOf
                ;

        allCustomers.remove(indexOf);
        return result;
    }
}