package com.smarthost.rom.business.occupancy;

import com.smarthost.rom.domain.RoomType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.smarthost.rom.domain.RoomType.ECONOMY;
import static com.smarthost.rom.domain.RoomType.PREMIUM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoomOccupancyManagerTest {

    private final RoomOccupancyManager occupancyManager = new RoomOccupancyManager();

    @ParameterizedTest
    @MethodSource
    public void validateOccupancyAlgorithm(List<Integer> customers, Map<RoomType, Integer> roomTypeMap,
                                           Map<RoomType, List<Integer>> expectedRoomOccupancy) {
        // Input collection could be unsorted.
        customers = new ArrayList<>(customers);
        Collections.shuffle(customers);

        var roomOccupancyByType = occupancyManager.allocateCustomers(customers, roomTypeMap);

        Assertions.assertThat(expectedRoomOccupancy.get(PREMIUM))
                .containsExactlyInAnyOrderElementsOf(roomOccupancyByType.get(PREMIUM));
        Assertions.assertThat(expectedRoomOccupancy.get(ECONOMY))
                .containsExactlyInAnyOrderElementsOf(roomOccupancyByType.get(ECONOMY));
    }

    @MethodSource
    private static Stream<Arguments> validateOccupancyAlgorithm() {
        return Stream.of(
                // One economy, one premium room, single customer of each type
                Arguments.of(List.of(90, 100), Map.of(PREMIUM, 1, ECONOMY, 1), Map.of(ECONOMY, List.of(90), PREMIUM, List.of(100))),
                // One economy, one premium room, multiple customers of each type, only wealthiest located
                Arguments.of(List.of(50, 90, 100, 200), Map.of(PREMIUM, 1, ECONOMY, 1), Map.of(ECONOMY, List.of(90), PREMIUM, List.of(200))),
                // One economy, one premium room, only premium customers, premium not downgraded
                Arguments.of(List.of(100, 200), Map.of(PREMIUM, 1, ECONOMY, 1), Map.of(ECONOMY, Collections.emptyList(), PREMIUM, List.of(200))),
                // One economy, one premium room, only economy customers, one economy upgraded to premium
                Arguments.of(List.of(50, 90), Map.of(PREMIUM, 1, ECONOMY, 1), Map.of(ECONOMY, List.of(50), PREMIUM, List.of(90))),
                // One economy, one premium room, single economy customer present, not upgraded
                Arguments.of(List.of(50), Map.of(PREMIUM, 1, ECONOMY, 1), Map.of(ECONOMY, List.of(50), PREMIUM, Collections.emptyList())),

                // No premium, One economy room, single premium customer, rooms left empty
                Arguments.of(List.of(500), Map.of(PREMIUM, 0, ECONOMY, 1), Map.of(ECONOMY, Collections.emptyList(), PREMIUM, Collections.emptyList())),
                // No economy, One premium room, single economy customer present, upgraded to premium
                Arguments.of(List.of(50), Map.of(PREMIUM, 1, ECONOMY, 0), Map.of(ECONOMY, Collections.emptyList(), PREMIUM, List.of(50))),
                // No economy, One premium room, single premium customer present
                Arguments.of(List.of(200), Map.of(PREMIUM, 1, ECONOMY, 0), Map.of(ECONOMY, Collections.emptyList(), PREMIUM, List.of(200))),
                // No customers, rooms left empty
                Arguments.of(Collections.emptyList(), Map.of(PREMIUM, 10, ECONOMY, 10), Map.of(ECONOMY, Collections.emptyList(), PREMIUM, Collections.emptyList())),
                // No rooms, left empty
                Arguments.of(List.of(50, 60, 70, 90, 100, 200, 300, 400), Map.of(PREMIUM, 0, ECONOMY, 0), Map.of(ECONOMY, Collections.emptyList(), PREMIUM, Collections.emptyList())),

                // Three economy rooms, three premium rooms, customers more, than rooms, wealthiest located
                Arguments.of(List.of(50, 55, 60, 90, 95, 100, 200, 300, 400), Map.of(PREMIUM, 3, ECONOMY, 3), Map.of(ECONOMY, List.of(60, 90, 95), PREMIUM, List.of(200, 300, 400))),
                // Three economy rooms, three premium rooms, economy customers less than rooms, premium fully occupied
                Arguments.of(List.of(50, 55, 100, 200, 300), Map.of(PREMIUM, 3, ECONOMY, 3), Map.of(ECONOMY, List.of(50, 55), PREMIUM, List.of(100, 200, 300))),
                // Three economy rooms, three premium rooms, premium customers less than rooms, economy fully occupied
                Arguments.of(List.of(50, 55, 60, 200, 300), Map.of(PREMIUM, 3, ECONOMY, 3), Map.of(ECONOMY, List.of(50, 55, 60), PREMIUM, List.of(200, 300))),
                // Three economy rooms, three premium rooms, not enough premium customers, rooms occupied by economy
                Arguments.of(List.of(50, 55, 60, 65, 90, 200, 300), Map.of(PREMIUM, 3, ECONOMY, 3), Map.of(ECONOMY, List.of(55, 60, 65), PREMIUM, List.of(90, 200, 300))),
                // Three economy rooms, three premium rooms, economy customers less than rooms present, not upgraded to premium
                Arguments.of(List.of(50, 55, 200, 300), Map.of(PREMIUM, 3, ECONOMY, 3), Map.of(ECONOMY, List.of(50, 55), PREMIUM, List.of(200, 300))),
                // Three economy rooms, three premium rooms, all customers economy, wealthiest upgraded
                Arguments.of(List.of(45, 50, 55, 60, 65, 70, 90, 95, 99), Map.of(PREMIUM, 3, ECONOMY, 3), Map.of(ECONOMY, List.of(60, 65, 70), PREMIUM, List.of(90, 95, 99))),

                // Multiple economy customers of same desired paying price, adjusted to premium
                Arguments.of(List.of(40, 45, 50, 55, 60, 60, 300, 400), Map.of(PREMIUM, 3, ECONOMY, 3), Map.of(ECONOMY, List.of(50, 55, 60), PREMIUM, List.of(60, 300, 400))),
                // Multiple economy customers of same desired paying price not assigned to economy
                Arguments.of(List.of(40, 45, 50, 55, 60, 300, 300, 300, 400), Map.of(PREMIUM, 3, ECONOMY, 3), Map.of(ECONOMY, List.of(50, 55, 60), PREMIUM, List.of(300, 300, 400))),
                // Only one economy upgraded
                Arguments.of(List.of(99, 99, 150), Map.of(PREMIUM, 3, ECONOMY, 1), Map.of(ECONOMY, List.of(99), PREMIUM, List.of(99, 150)))
                );
    }

    @ParameterizedTest
    @MethodSource
    public void checkInvalidArgumentsException(List<Integer> customers, Map<RoomType, Integer> roomTypeMap, String message) {
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> occupancyManager.allocateCustomers(customers, roomTypeMap));
        assertEquals(message, illegalArgumentException.getMessage());
    }

    @MethodSource
    private static Stream<Arguments> checkInvalidArgumentsException() {
        return Stream.of(
                // Null customers list
                Arguments.of(null, Map.of(PREMIUM, 1, ECONOMY, 1), "Customers list cannot be null"),
                // Negative economy rooms count
                Arguments.of(List.of(50), Map.of(PREMIUM, -1, ECONOMY, 1), "Rooms count should be present and cannot be negative"),
                // Negative premium rooms count
                Arguments.of(List.of(50), Map.of(PREMIUM, 1, ECONOMY, -1), "Rooms count should be present and cannot be negative"),
                // Null economy rooms count
                Arguments.of(List.of(50), Map.of(PREMIUM, 1), "Rooms count should be present and cannot be negative"),
                // Null premium rooms count
                Arguments.of(List.of(50), Map.of(ECONOMY, 1), "Rooms count should be present and cannot be negative")
        );
    }

}
