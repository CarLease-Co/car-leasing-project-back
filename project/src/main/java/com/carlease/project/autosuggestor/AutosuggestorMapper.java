package com.carlease.project.autosuggestor;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AutosuggestorMapper {

    AutosuggestorMapper INSTANCE = Mappers.getMapper( AutosuggestorMapper.class );
    AutosuggestorDto autosuggestorToAutosuggestorDto(Autosuggestor autosuggestor);
    Autosuggestor autosuggestorDtoToAutosuggestor(AutosuggestorDto autosuggestorDto);
}
