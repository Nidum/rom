package com.smarthost.rom.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

public enum RoomType {
    ECONOMY(1, 100),
    PREMIUM(100, Integer.MAX_VALUE),
    NONE(Integer.MIN_VALUE, Integer.MAX_VALUE);

    private int lowerBoundary;
    private int upperBoundary;

    RoomType(int lowerBoundary, int upperBoundary) {
        this.lowerBoundary = lowerBoundary;
        this.upperBoundary = upperBoundary;
    }

    public int getLowerBoundary() {
        return lowerBoundary;
    }

    public int getUpperBoundary() {
        return upperBoundary;
    }

    private void setLowerBoundary(int lowerBoundary) {
        this.lowerBoundary = lowerBoundary;
    }

    private void setUpperBoundary(int upperBoundary) {
        this.upperBoundary = upperBoundary;
    }

    @Configuration
    @PropertySource(value={"classpath:price.properties"})
    public static class PriceConfig {
        @Value("${economy.lowerBound:1}")
        private int economyLowerBound;
        @Value("${economy.upperBound:100}")
        private int economyUpperBound;

        @PostConstruct
        public void postConstruct() {
            ECONOMY.setLowerBoundary(economyLowerBound);
            ECONOMY.setUpperBoundary(economyUpperBound);
            PREMIUM.setLowerBoundary(economyUpperBound);
        }
    }
}
