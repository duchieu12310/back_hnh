package com.hnh.controller.statistic;

import com.hnh.constant.AppConstants;
import com.hnh.dto.statistic.StatisticResponse;
import com.hnh.service.statistic.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class StatisticController {

    private StatisticService statisticService;

    @GetMapping
    public ResponseEntity<StatisticResponse> getStatistic(@RequestParam(value = "period", defaultValue = "month") String period) {
        return ResponseEntity.status(HttpStatus.OK).body(statisticService.getStatistic(period));
    }

}

