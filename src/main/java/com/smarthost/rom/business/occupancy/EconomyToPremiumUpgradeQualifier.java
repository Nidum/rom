package com.smarthost.rom.business.occupancy;

import com.smarthost.rom.domain.RoomType;

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
        this.allCustomers = allCustomers;
    }

    @Override
    protected RoomType getType() {
        return RoomType.PREMIUM;
    }

    @Override
    protected boolean customerMatchesRoom(int wishedPayment) {
        var customersLeft = allCustomers.indexOf(wishedPayment);
        return customersLeft >= roomCountMap.get(ECONOMY) && wishedPayment < getType().getLowerBoundary() &&
                roomCountMap.get(PREMIUM) > premiumCustomers.size();
    }
}