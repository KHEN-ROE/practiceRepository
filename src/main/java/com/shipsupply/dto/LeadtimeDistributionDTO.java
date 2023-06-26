package com.shipsupply.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LeadtimeDistributionDTO {
    Long leadtime;
    Long leadtimeCount;
}
