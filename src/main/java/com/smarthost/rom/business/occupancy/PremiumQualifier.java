package com.smarthost.rom.business.occupancy;

import com.smarthost.rom.domain.RoomType;

import java.util.List;
import java.util.Map;

import static com.smarthost.rom.domain.RoomType.PREMIUM;

class PremiumQualifier extends CustomerTypeQualifier {

    private final List<Integer> premiumCustomers;

    public PremiumQualifier(Map<RoomType, Integer> roomCountMap, List<Integer> premiumCustomers,
                            CustomerTypeQualifier next) {
        super(roomCountMap, next);
        this.premiumCustomers = premiumCustomers;
    }

    @Override
    protected RoomType getType() {
        return PREMIUM;
    }

    @Override
    protected boolean customerMatchesRoom(int wishedPayment) {
        return roomCountMap.get(PREMIUM) > premiumCustomers.size() && wishedPayment >= getType().getLowerBoundary();
    }
}
