package hr.rba.rba_it.dto;

import hr.rba.rba_it.enumeration.CardStatus;
import hr.rba.rba_it.validation.ValidOib;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Request to create a card request")
public record CardRequestRequest(

        @Schema(description = "First name of the applicant", example = "Ivan")
        @NotBlank(message = "Value firstName must be filled")
        String firstName,

        @Schema(description = "Last name of the applicant", example = "Horvat")
        @NotBlank(message = "Value lastName must be filled")
        String lastName,

        @Schema(description = "OIB of the applicant", example = "73660371074")
        @NotBlank(message = "Value oib must be filled")
        @ValidOib
        String oib,

        @Schema(description = "Status of the card request", enumAsRef = true, example = "PENDING")
        CardStatus status
) {
}