package com.smarthost.rom.service;

import com.smarthost.rom.business.occupancy.RoomOccupancyManager;
import com.smarthost.rom.dto.RoomOccupancyResponse;
import com.smarthost.rom.repository.UserPayWillingnessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smarthost.rom.domain.RoomType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoomOccupancyServiceTest {

    private final UserPayWillingnessRepository payWillingnessRepository = mock(UserPayWillingnessRepository.class);
    private final RoomOccupancyManager occupancyManager = mock(RoomOccupancyManager.class);

    private final RoomOccupancyService roomOccupancyService =
            new RoomOccupancyService(payWillingnessRepository, occupancyManager);

    @BeforeEach
    public void init() {
        when(payWillingnessRepository.getCustomers())
                .thenReturn(Collections.emptyList());
    }

    @Test
    public void negativeIsInvalidForEconomy() {
        assertThrows(IllegalArgumentException.class, () -> roomOccupancyService.getOccupancy(-1, 1));
    }

    @Test
    public void negativeIsInvalidForPremium() {
        assertThrows(IllegalArgumentException.class, () -> roomOccupancyService.getOccupancy(1, -1));
    }

    @Test
    public void onlyEconomyRooms() {
        when(occupancyManager.allocateCustomers(any(), any()))
                .thenReturn(new HashMap<>(Map.of(ECONOMY, List.of(50, 50, 50))));
        var actualResponse = roomOccupancyService.getOccupancy(3, 1);
        var expectedResponse = new RoomOccupancyResponse(3, 0, 150);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void onlyPremiumRooms() {
        when(occupancyManager.allocateCustomers(any(), any()))
                .thenReturn(new HashMap<>(Map.of(PREMIUM, List.of(150, 150, 150))));
        var actualResponse = roomOccupancyService.getOccupancy(1, 3);
        var expectedResponse = new RoomOccupancyResponse(0, 3, 450);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void differentRoomTypes() {
        when(occupancyManager.allocateCustomers(any(), any()))
                .thenReturn(new HashMap<>(Map.of(ECONOMY, List.of(50, 50, 50), PREMIUM, List.of(150, 150, 150))));
        var actualResponse = roomOccupancyService.getOccupancy(3, 3);
        var expectedResponse = new RoomOccupancyResponse(3, 3, 600);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void noCustomersAvailable() {
        when(occupancyManager.allocateCustomers(any(), any()))
                .thenReturn(new HashMap<>());
        var actualResponse = roomOccupancyService.getOccupancy(3, 3);
        var expectedResponse = new RoomOccupancyResponse(0, 0, 0);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void noRoomsAvailable() {
        when(occupancyManager.allocateCustomers(any(), any()))
                .thenReturn(new HashMap<>());
        var actualResponse = roomOccupancyService.getOccupancy(0, 0);
        var expectedResponse = new RoomOccupancyResponse(0, 0, 0);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void noneRoomTypeIgnored() {
        when(occupancyManager.allocateCustomers(any(), any()))
                .thenReturn(new HashMap<>(Map.of(ECONOMY, List.of(50, 50, 50), PREMIUM, List.of(150, 150, 150),
                        NONE, List.of(45, 45, 45))));
        var actualResponse = roomOccupancyService.getOccupancy(3, 3);
        var expectedResponse = new RoomOccupancyResponse(3, 3, 600);
        assertEquals(actualResponse, expectedResponse);
    }

}
