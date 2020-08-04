package com.smarthost.rom.domain;

import com.google.common.collect.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

import static com.smarthost.rom.exception.Messages.PRICE_BOUNDARIES_VALIDATION;

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

    private void setBoundaries(Range<Integer> boundaries) {
        this.lowerBoundary = boundaries.lowerEndpoint();
        this.upperBoundary = boundaries.upperEndpoint();
    }

    @Configuration
    @PropertySource(value = {"classpath:price.properties"})
    public static class PriceConfig {
        @Value("${economy.lowerBound:1}")
        private int economyLowerBound;
        @Value("${economy.upperBound:100}")
        private int economyUpperBound;
        @Value("${premium.lowerBound:100}")
        private int premiumLowerBound;
        @Value("${premium.upperBound:#{T(java.lang.Integer).MAX_VALUE}}")
        private int premiumUpperBound;

        @PostConstruct
        public void postConstruct() {
            var economyRange = Range.closedOpen(economyLowerBound, economyUpperBound);
            var premiumRange = Range.closedOpen(premiumLowerBound, premiumUpperBound);

            if (!economyRange.intersection(premiumRange).isEmpty()) {
                throw new IllegalArgumentException(PRICE_BOUNDARIES_VALIDATION);
            }

            ECONOMY.setBoundaries(economyRange);
            PREMIUM.setBoundaries(premiumRange);
        }
    }
}
