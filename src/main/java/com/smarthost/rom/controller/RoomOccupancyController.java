package com.smarthost.rom.controller;

import com.smarthost.rom.dto.RoomOccupancyResponse;
import com.smarthost.rom.service.RoomOccupancyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

@Api(tags = {"occupancy"})
@Validated
@RestController
@RequestMapping("/api/room/occupancy")
public class RoomOccupancyController {

    private final RoomOccupancyService roomOccupancyService;

    public RoomOccupancyController(RoomOccupancyService roomOccupancyService) {
        this.roomOccupancyService = roomOccupancyService;
    }

    @ApiOperation("Count occupation for requested count of economy and premium rooms and calculate final gain")
    @GetMapping
    public RoomOccupancyResponse getOccupancy(@Valid @NotNull @PositiveOrZero(message = ECONOMY_ONLY_POSITIVE_MSG)
                                              @RequestParam(required = false, defaultValue = "0")
                                              @ApiParam("Available economy rooms count") Integer economy,
                                              @Valid @NotNull @PositiveOrZero(message = PREMIUM_ONLY_POSITIVE_MSG)
                                              @RequestParam(required = false, defaultValue = "0")
                                              @ApiParam("Available premium rooms count") Integer premium) {
        return roomOccupancyService.getOccupancy(economy, premium);
    }

}

