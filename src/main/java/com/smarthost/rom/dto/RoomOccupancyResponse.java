package com.smarthost.rom.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class RoomOccupancyResponse {
    @ApiModelProperty(value = "Count of economy rooms occupied", position = 1)
    private final int economy;
    @ApiModelProperty(value = "Count of premium rooms occupied", position = 2)
    private final int premium;
    @ApiModelProperty(value = "Final gain from occupied rooms", position = 3)
    private final int gain;

    public RoomOccupancyResponse(int economy, int premium, int gain) {
        this.economy = economy;
        this.premium = premium;
        this.gain = gain;
    }

    public int getEconomy() {
        return economy;
    }

    public int getPremium() {
        return premium;
    }

    public int getGain() {
        return gain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomOccupancyResponse that = (RoomOccupancyResponse) o;
        return economy == that.economy &&
                premium == that.premium &&
                gain == that.gain;
    }

    @Override
    public int hashCode() {
        return Objects.hash(economy, premium, gain);
    }

    @Override
    public String toString() {
        return "RoomOccupancyResponse{" +
                "economy=" + economy +
                ", premium=" + premium +
                ", gain=" + gain +
                '}';
    }
}
