package com.carlease.project.application;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    ApplicationMapper INSTANCE = Mappers.getMapper( ApplicationMapper.class );

    //add fields here
    ApplicationFormDto applicationToApplicationDto(Application application);
    Application applicationFormDtoToApplication(ApplicationFormDto applicationFormDto);

}
