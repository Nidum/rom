package com.smarthost.rom.controller;

import com.smarthost.rom.dto.RoomOccupancyResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import static com.smarthost.rom.exception.Messages.ECONOMY_ONLY_POSITIVE_MSG;
import static com.smarthost.rom.exception.Messages.PREMIUM_ONLY_POSITIVE_MSG;

@Validated
@RestController
@RequestMapping("/api/room/occupancy")
public class RoomOccupancyController {

    @GetMapping
    public RoomOccupancyResponse getOccupancy(@Valid @NotNull @PositiveOrZero(message = ECONOMY_ONLY_POSITIVE_MSG)
                                              @RequestParam(required = false, defaultValue = "0") Integer economy,
                                              @Valid @NotNull @PositiveOrZero(message = PREMIUM_ONLY_POSITIVE_MSG)
                                              @RequestParam(required = false, defaultValue = "0") Integer premium) {

        return null;
    }

}

