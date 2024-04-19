package com.carlease.project.application;

import org.mapstruct.factory.Mappers;

public interface ApplicationMapper {

    ApplicationMapper INSTANCE = Mappers.getMapper( ApplicationMapper.class );

    //add fields here
    ApplicationFormDto applicationToApplicationDto(Application application);

}
