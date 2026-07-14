package hr.rba.rba_it.dto;

import hr.rba.rba_it.enumeration.CardStatus;

public record CardRequestStatusChangeMqRequest(
        String oib,
        CardStatus status
) {
}