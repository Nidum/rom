package com.smarthost.rom.entity;

public class Customer {
    private final int wishedPayment;

    public Customer(int wishedPayment) {
        this.wishedPayment = wishedPayment;
    }

    public int getWishedPayment() {
        return wishedPayment;
    }
}
