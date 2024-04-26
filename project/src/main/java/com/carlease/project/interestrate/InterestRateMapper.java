package com.carlease.project.interestrate;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InterestRateMapper {

InterestRateMapper INSTANCE = Mappers.getMapper(InterestRateMapper.class);
    InterestRate toEntity(InterestRateDTO interestRateDTO);

    InterestRateDTO toDto(InterestRate interestRate);
}
