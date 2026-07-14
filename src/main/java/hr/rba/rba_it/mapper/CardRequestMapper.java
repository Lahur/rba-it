package hr.rba.rba_it.mapper;

import hr.rba.rba_it.dto.CardRequestRequest;
import hr.rba.rba_it.dto.CardRequestResponse;
import hr.rba.rba_it.model.CardRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardRequestMapper {

    @Mapping(target = "id", ignore = true)
    CardRequestEntity toEntity(CardRequestRequest request);

    CardRequestResponse toResponse(CardRequestEntity entity);

    List<CardRequestResponse> toResponseList(List<CardRequestEntity> entities);

    @Mapping(target = "id", ignore = true)
    void updateEntity(CardRequestRequest request, @MappingTarget CardRequestEntity entity);
}