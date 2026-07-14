package hr.rba.rba_it.service;

import hr.rba.rba_it.dao.CardRequestRepository;
import hr.rba.rba_it.dto.CardRequestRequest;
import hr.rba.rba_it.dto.CardRequestResponse;
import hr.rba.rba_it.enumeration.CardStatus;
import hr.rba.rba_it.exception.NotFoundException;
import hr.rba.rba_it.mapper.CardRequestMapper;
import hr.rba.rba_it.model.CardRequestEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardRequestServiceTest {

    private static final String OIB = "00111111113";

    @Mock
    private CardRequestRepository repository;

    @Mock
    private CardRequestMapper cardRequestMapper;

    @InjectMocks
    private CardRequestService service;

    @Test
    void findAll_returnsMappedResponses() {
        CardRequestEntity entity = entity();
        CardRequestResponse response = response();
        when(repository.findAll()).thenReturn(List.of(entity));
        when(cardRequestMapper.toResponseList(List.of(entity))).thenReturn(List.of(response));

        List<CardRequestResponse> result = service.findAll();

        assertThat(result).containsExactly(response);
    }

    @Test
    void findByOib_whenCardRequestExists_returnsMappedResponse() {
        CardRequestEntity entity = entity();
        CardRequestResponse response = response();
        when(repository.findByOib(OIB)).thenReturn(Optional.of(entity));
        when(cardRequestMapper.toResponse(entity)).thenReturn(response);

        CardRequestResponse result = service.findByOib(OIB);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void findByOib_whenCardRequestDoesNotExist_throwsNotFoundException() {
        when(repository.findByOib(OIB)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByOib(OIB))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Card request with oib %s not found".formatted(OIB));
    }

    @Test
    void save_mapsRequestToEntityAndReturnsMappedResponse() {
        CardRequestRequest request = CardRequestRequest.builder()
                .firstName("Ana")
                .lastName("Anic")
                .oib(OIB)
                .status(CardStatus.PENDING)
                .build();
        CardRequestEntity entityToSave = CardRequestEntity.builder()
                .firstName("Ana")
                .lastName("Anic")
                .oib(OIB)
                .status(CardStatus.PENDING)
                .build();
        CardRequestEntity savedEntity = entity();
        CardRequestResponse response = response();
        when(cardRequestMapper.toEntity(request)).thenReturn(entityToSave);
        when(repository.save(entityToSave)).thenReturn(savedEntity);
        when(cardRequestMapper.toResponse(savedEntity)).thenReturn(response);

        CardRequestResponse result = service.save(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void deleteByOib_delegatesToRepository() {
        service.deleteByOib(OIB);

        verify(repository).deleteByOib(OIB);
    }

    @Test
    void updateStatus_whenCardRequestExists_updatesAndSavesEntity() {
        CardRequestEntity entity = entity();
        when(repository.findByOib(OIB)).thenReturn(Optional.of(entity));
        when(cardRequestMapper.toResponse(any(CardRequestEntity.class))).thenReturn(response());

        service.updateStatus(OIB, CardStatus.APPROVED);

        ArgumentCaptor<CardRequestEntity> captor = ArgumentCaptor.forClass(CardRequestEntity.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(CardStatus.APPROVED);
    }

    @Test
    void updateStatus_whenCardRequestDoesNotExist_throwsNotFoundExceptionAndDoesNotSave() {
        when(repository.findByOib(OIB)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStatus(OIB, CardStatus.APPROVED))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Card request with oib %s not found".formatted(OIB));

        verify(repository, never()).save(any());
    }

    private CardRequestEntity entity() {
        return CardRequestEntity.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .firstName("Ana")
                .lastName("Anic")
                .oib(OIB)
                .status(CardStatus.PENDING)
                .build();
    }

    private CardRequestResponse response() {
        return CardRequestResponse.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .firstName("Ana")
                .lastName("Anic")
                .oib(OIB)
                .status(CardStatus.PENDING)
                .build();
    }
}