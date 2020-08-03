package com.smarthost.rom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthost.rom.dto.RoomOccupancyResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

import static com.smarthost.rom.exception.Messages.ECONOMY_ONLY_POSITIVE_MSG;
import static com.smarthost.rom.exception.Messages.PREMIUM_ONLY_POSITIVE_MSG;
import static org.hamcrest.Matchers.*;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomOccupancyControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource
    public void controllerNegativeCases(String economy, String premium, List<String> messages) throws Exception {
        mockMvc.perform(get("/api/room/occupancy")
                .param("economy", economy)
                .param("premium", premium))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages")
                        .value(containsInAnyOrder(messages.toArray())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status")
                        .value(400));
    }

    private static Stream<Arguments> controllerNegativeCases() {
        return Stream.of(
                Arguments.of("-1", "1", List.of(ECONOMY_ONLY_POSITIVE_MSG)),
                Arguments.of("1", "-1", List.of(PREMIUM_ONLY_POSITIVE_MSG)),
                Arguments.of("-1", "-1", List.of(ECONOMY_ONLY_POSITIVE_MSG, PREMIUM_ONLY_POSITIVE_MSG)),
                Arguments.of("test", "1", List.of("Value 'test' is invalid for field economy")),
                Arguments.of("1", "test", List.of("Value 'test' is invalid for field premium"))
        );
    }

    @ParameterizedTest
    @MethodSource
    public void controllerPositiveCases(String economy, String premium, RoomOccupancyResponse response) throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room/occupancy")
                .param("economy", economy)
                .param("premium", premium))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals(actualResponse, mapper.writeValueAsString(response), STRICT);
    }

    private static Stream<Arguments> controllerPositiveCases() {
        return Stream.of(
                Arguments.of("1", "1", new RoomOccupancyResponse(1, 1, 699)),
                Arguments.of("0", "0", new RoomOccupancyResponse(0, 0, 0)),
                Arguments.of("0", "1", new RoomOccupancyResponse(0, 1, 600)),
                Arguments.of("1", "0", new RoomOccupancyResponse(1, 0, 99)),
                Arguments.of(null, "1", new RoomOccupancyResponse(0, 1, 600)),
                Arguments.of("1", null, new RoomOccupancyResponse(1, 0, 99))
        );
    }

}
