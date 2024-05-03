package com.carlease.project.autosuggestor;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AutosuggestorMapper {

    AutosuggestorMapper INSTANCE = Mappers.getMapper( AutosuggestorMapper.class );
    AutosuggestorDto toDto(Autosuggestor autosuggestor);
    Autosuggestor toEntity(AutosuggestorDto autosuggestorDto);
}
