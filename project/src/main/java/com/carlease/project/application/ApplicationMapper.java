package com.carlease.project.application;

import com.carlease.project.car.Car;
import com.carlease.project.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    ApplicationMapper INSTANCE = Mappers.getMapper(ApplicationMapper.class);

    @Mapping(target = "carMake", source = "car.make")
    @Mapping(target = "carModel", source = "car.model")
    @Mapping(target = "userId", source = "user.userId")
    ApplicationFormDto toDto(Application application);

    @Mapping(target = "car.make", source = "carMake")
    @Mapping(target = "car.model", source = "carModel")
    @Mapping(target = "user.userId", source = "userId")
    Application toEntity(ApplicationFormDto applicationFormDto);
}
