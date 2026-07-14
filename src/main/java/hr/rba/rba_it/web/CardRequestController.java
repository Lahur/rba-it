package hr.rba.rba_it.web;

import hr.rba.rba_it.dto.CardRequestRequest;
import hr.rba.rba_it.dto.CardRequestResponse;
import hr.rba.rba_it.exception.NotFoundException;
import hr.rba.rba_it.service.CardRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/card-request")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
@Tag(name = "Card Request", description = "Endpoints for managing card requests")
public class CardRequestController {

    private final CardRequestService service;

    @GetMapping
    @Operation(summary = "Get all card requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card requests retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<CardRequestResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{oib}")
    @Operation(summary = "Get card request by OIB")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card request retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No card request found for the given OIB"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CardRequestResponse> findByOib(@Parameter(description = "OIB of the applicant") @PathVariable String oib) {
        try {
            return ResponseEntity.ok(service.findByOib(oib));
        }
        catch (NotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new card request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Card request created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CardRequestResponse> save(@RequestBody @Validated CardRequestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @DeleteMapping("/{oib}")
    @Operation(summary = "Delete a card request by OIB")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Card request deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteByOib(@Parameter(description = "OIB of the applicant") @PathVariable String oib) {
        service.deleteByOib(oib);
        return ResponseEntity.noContent().build();
    }
}