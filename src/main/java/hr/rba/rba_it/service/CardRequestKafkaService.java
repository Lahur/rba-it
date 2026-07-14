package hr.rba.rba_it.service;

import tools.jackson.databind.ObjectMapper;
import hr.rba.rba_it.dto.CardRequestStatusChangeMqRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardRequestKafkaService {

    private final CardRequestService cardRequestService;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic.card-status-change}")
    public void onStatusChange(String payload) {
        CardRequestStatusChangeMqRequest request = objectMapper.readValue(payload, CardRequestStatusChangeMqRequest.class);
        log.info("Received status change for oib {}: {}", request.oib(), request.status());
        cardRequestService.updateStatus(request.oib(), request.status());
    }
}