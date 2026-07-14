package hr.rba.rba_it.dto;

import hr.rba.rba_it.enumeration.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Card request details")
public record CardRequestResponse(

        @Schema(description = "Unique identifier of the card request")
        UUID id,

        @Schema(description = "First name of the applicant", example = "Ivan")
        String firstName,

        @Schema(description = "Last name of the applicant", example = "Horvat")
        String lastName,

        @Schema(description = "OIB of the applicant", example = "73660371074")
        String oib,

        @Schema(description = "Status of the card request", enumAsRef = true, example = "PENDING")
        CardStatus status
) {
}