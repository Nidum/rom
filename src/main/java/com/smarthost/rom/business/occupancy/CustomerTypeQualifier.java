package com.smarthost.rom.business.occupancy;

import com.smarthost.rom.domain.RoomType;

import java.util.Map;

import static com.smarthost.rom.domain.RoomType.NONE;

public abstract class CustomerTypeQualifier {

    protected final Map<RoomType, Integer> roomCountMap;
    protected final CustomerTypeQualifier next;

    protected CustomerTypeQualifier(Map<RoomType, Integer> roomCountMap, CustomerTypeQualifier next) {
        this.roomCountMap = roomCountMap;
        this.next = next;
    }

    protected abstract RoomType getType();

    protected abstract boolean customerMatchesRoom(int customer);

    public RoomType customerRoomType(int wishedPayment) {
        return customerMatchesRoom(wishedPayment) ? this.getType() :
                next == null ? NONE : next.customerRoomType(wishedPayment);
    }
}
