package hr.rba.rba_it.web;

import tools.jackson.databind.ObjectMapper;
import hr.rba.rba_it.dao.CardRequestRepository;
import hr.rba.rba_it.dto.CardRequestRequest;
import hr.rba.rba_it.enumeration.CardStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Card requests are seeded once per context from {@code test-data.sql}; each test runs in
 * its own rolled-back transaction, so mutations here never leak into other tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class CardRequestControllerIntegrationTest {

    private static final String BASE_URL = "/v1/card-request";

    // Seeded by test-data.sql
    private static final String SEEDED_OIB_1 = "00111111113";
    private static final String SEEDED_ID_1 = "11111111-1111-1111-1111-111111111111";
    private static final String SEEDED_OIB_2 = "00222222224";

    private static final String UNUSED_VALID_OIB = "00333333335";
    private static final String NEW_VALID_OIB = "00000000010";
    private static final String INVALID_OIB = "12345678901";

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private CardRequestRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_returnsSeededCardRequests() {
        MvcTestResult result = mvc.get().uri(BASE_URL).exchange();

        assertThat(result).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON);
        assertThat(result).bodyJson().extractingPath("$").asArray().hasSize(2);
        assertThat(result).bodyJson().extractingPath("$[*].oib").asArray()
                .containsExactlyInAnyOrder(SEEDED_OIB_1, SEEDED_OIB_2);
    }

    @Test
    void findByOib_whenCardRequestExists_returnsIt() {
        MvcTestResult result = mvc.get().uri(BASE_URL + "/{oib}", SEEDED_OIB_1).exchange();

        assertThat(result).hasStatusOk();
        assertThat(result).bodyJson().extractingPath("$.id").asString().isEqualTo(SEEDED_ID_1);
        assertThat(result).bodyJson().extractingPath("$.firstName").asString().isEqualTo("Ana");
        assertThat(result).bodyJson().extractingPath("$.lastName").asString().isEqualTo("Anic");
        assertThat(result).bodyJson().extractingPath("$.oib").asString().isEqualTo(SEEDED_OIB_1);
        assertThat(result).bodyJson().extractingPath("$.status").asString().isEqualTo("PENDING");
    }

    @Test
    void findByOib_whenCardRequestDoesNotExist_returnsNoContent() {
        MvcTestResult result = mvc.get().uri(BASE_URL + "/{oib}", UNUSED_VALID_OIB).exchange();

        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void save_withValidRequest_persistsAndReturnsCreatedCardRequest() {
        CardRequestRequest request = CardRequestRequest.builder()
                .firstName("Cvijeta")
                .lastName("Cvijic")
                .oib(NEW_VALID_OIB)
                .status(CardStatus.PENDING)
                .build();

        MvcTestResult result = mvc.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result).hasStatus(HttpStatus.CREATED);
        assertThat(result).bodyJson().extractingPath("$.id").isNotNull();
        assertThat(result).bodyJson().extractingPath("$.firstName").asString().isEqualTo("Cvijeta");
        assertThat(result).bodyJson().extractingPath("$.lastName").asString().isEqualTo("Cvijic");
        assertThat(result).bodyJson().extractingPath("$.oib").asString().isEqualTo(NEW_VALID_OIB);
        assertThat(result).bodyJson().extractingPath("$.status").asString().isEqualTo("PENDING");

        assertThat(repository.findByOib(NEW_VALID_OIB))
                .isPresent()
                .get()
                .satisfies(entity -> {
                    assertThat(entity.getFirstName()).isEqualTo("Cvijeta");
                    assertThat(entity.getLastName()).isEqualTo("Cvijic");
                    assertThat(entity.getStatus()).isEqualTo(CardStatus.PENDING);
                });
    }

    @Test
    void save_withBlankFirstName_returnsBadRequest() {
        CardRequestRequest request = CardRequestRequest.builder()
                .firstName(" ")
                .lastName("Cvijic")
                .oib(NEW_VALID_OIB)
                .status(CardStatus.PENDING)
                .build();

        MvcTestResult result = mvc.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(repository.findByOib(NEW_VALID_OIB)).isEmpty();
    }

    @Test
    void save_withInvalidOib_returnsBadRequest() {
        CardRequestRequest request = CardRequestRequest.builder()
                .firstName("Cvijeta")
                .lastName("Cvijic")
                .oib(INVALID_OIB)
                .status(CardStatus.PENDING)
                .build();

        MvcTestResult result = mvc.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(repository.findByOib(INVALID_OIB)).isEmpty();
    }

    @Test
    void deleteByOib_removesSeededCardRequest() {
        MvcTestResult result = mvc.delete().uri(BASE_URL + "/{oib}", SEEDED_OIB_1).exchange();

        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
        assertThat(repository.findByOib(SEEDED_OIB_1)).isEmpty();
    }

    @Test
    void deleteByOib_whenCardRequestDoesNotExist_returnsNoContent() {
        MvcTestResult result = mvc.delete().uri(BASE_URL + "/{oib}", UNUSED_VALID_OIB).exchange();

        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }
}