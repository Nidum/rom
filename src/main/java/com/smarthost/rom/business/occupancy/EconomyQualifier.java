package com.smarthost.rom.business.occupancy;

import com.smarthost.rom.domain.RoomType;

import java.util.List;
import java.util.Map;

class EconomyQualifier extends CustomerTypeQualifier {

    private final List<Integer> economyCustomers;

    public EconomyQualifier(Map<RoomType, Integer> roomCountMap, List<Integer> economyCustomers, CustomerTypeQualifier next) {
        super(roomCountMap, next);
        this.economyCustomers = economyCustomers;
    }

    @Override
    protected RoomType getType() {
        return RoomType.ECONOMY;
    }

    @Override
    protected boolean customerMatchesRoom(int wishedPayment) {
        return roomCountMap.get(RoomType.ECONOMY) > economyCustomers.size() && wishedPayment < getType().getUpperBoundary();
    }
}
