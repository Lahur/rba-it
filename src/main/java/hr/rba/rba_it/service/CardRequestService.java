package hr.rba.rba_it.service;

import hr.rba.rba_it.dao.CardRequestRepository;
import hr.rba.rba_it.dto.CardRequestRequest;
import hr.rba.rba_it.dto.CardRequestResponse;
import hr.rba.rba_it.enumeration.CardStatus;
import hr.rba.rba_it.exception.NotFoundException;
import hr.rba.rba_it.mapper.CardRequestMapper;
import hr.rba.rba_it.model.CardRequestEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardRequestService {

    private final CardRequestRepository repository;

    private final CardRequestMapper cardRequestMapper;

    public List<CardRequestResponse> findAll() {
        return cardRequestMapper.toResponseList(repository.findAll());
    }

    public CardRequestResponse findByOib(String oib) {
        log.debug("Fetching card request by oib {}", oib);
        CardRequestResponse cardRequestResponse = repository.findByOib(oib)
                .map(cardRequestMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Card request with oib %s not found".formatted(oib)));
        log.debug("Found card request by oib {}", oib);
        log.trace("CardRequest body: {}", cardRequestResponse);
        return cardRequestResponse;
    }

    public CardRequestResponse save(CardRequestRequest request) {
        log.debug("Saving new card request");
        log.trace("CardRequest body: {}", request);
        return cardRequestMapper.toResponse(repository.save(cardRequestMapper.toEntity(request)));
    }

    @Transactional
    public void deleteByOib(String oib) {
        log.debug("Removing card request with oib {}", oib);
        repository.deleteByOib(oib);
    }

    public void updateStatus(String oib, CardStatus status) {
        log.debug("Updating card request with oib {} to status {}", oib, status);
        CardRequestEntity entity = repository.findByOib(oib)
                .orElseThrow(() -> new NotFoundException("Card request with oib %s not found".formatted(oib)));
        log.debug("Found card request by oib {}", oib);
        log.trace("CardRequest body: {}", cardRequestMapper.toResponse(entity));
        entity.setStatus(status);
        repository.save(entity);
    }
}
