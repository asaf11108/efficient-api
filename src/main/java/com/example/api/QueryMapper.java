package com.example.api;

import org.mapstruct.Mapper;
import org.mapstruct.control.MappingControl.Use;

import java.util.Map;

import org.graalvm.polyglot.*;
import org.mapstruct.factory.Mappers;

@Mapper (uses = {Map.class})
public interface QueryMapper {
 
    QueryMapper INSTANCE = Mappers.getMapper( QueryMapper.class );
 
    QueryDto valueToQueryDto(Value value);
    
}
