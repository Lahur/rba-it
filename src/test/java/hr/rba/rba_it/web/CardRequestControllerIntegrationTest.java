package hr.rba.rba_it.web;

import hr.rba.rba_it.dto.CardRequestResponse;
import tools.jackson.core.type.TypeReference;
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

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class CardRequestControllerIntegrationTest {

    private static final String BASE_URL = "/v1/card-request";

    private static final String EXISTING_OIB_1 = "00111111113";
    private static final String EXISTING_ID_1 = "11111111-1111-1111-1111-111111111111";
    private static final String EXISTING_OIB_2 = "00222222224";

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
    void findAll_returnsExistingCardRequests() throws UnsupportedEncodingException {
        MvcTestResult result = mvc.get().uri(BASE_URL).exchange();

        List<CardRequestResponse> responses = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertThat(result).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON);
        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(CardRequestResponse::oib)
                .containsExactlyInAnyOrder(EXISTING_OIB_1, EXISTING_OIB_2);
    }

    @Test
    void findByOib_whenCardRequestExists_returnsIt() throws UnsupportedEncodingException {
        MvcTestResult result = mvc.get().uri(BASE_URL + "/{oib}", EXISTING_OIB_1).exchange();

        CardRequestResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                CardRequestResponse.class);

        assertThat(result).hasStatusOk();
        assertThat(response).extracting(
                r -> r.id().toString(),
                CardRequestResponse::firstName,
                CardRequestResponse::lastName,
                CardRequestResponse::oib,
                CardRequestResponse::status)
            .containsExactly(EXISTING_ID_1, "Ana", "Anic", EXISTING_OIB_1, CardStatus.PENDING);
    }

    @Test
    void findByOib_whenCardRequestDoesNotExist_returnsNoContent() {
        MvcTestResult result = mvc.get().uri(BASE_URL + "/{oib}", UNUSED_VALID_OIB).exchange();

        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void save_withValidRequest_persistsAndReturnsCreatedCardRequest() throws UnsupportedEncodingException {
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

        CardRequestResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), CardRequestResponse.class);

        assertThat(result).hasStatus(HttpStatus.CREATED);
        assertThat(response).extracting(
                CardRequestResponse::firstName,
                CardRequestResponse::lastName,
                CardRequestResponse::oib,
                CardRequestResponse::status)
            .containsExactly("Cvijeta", "Cvijic", NEW_VALID_OIB, CardStatus.PENDING);
        assertThat(response.id()).isNotNull();

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
    void save_withAlreadyUsedOib_returnsConflict() {
        CardRequestRequest request = CardRequestRequest.builder()
                .firstName("Cvijeta")
                .lastName("Cvijic")
                .oib(EXISTING_OIB_1)
                .status(CardStatus.PENDING)
                .build();

        MvcTestResult result = mvc.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result).hasStatus(HttpStatus.CONFLICT);
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
    void deleteByOib_removesExistingCardRequest() {
        MvcTestResult result = mvc.delete().uri(BASE_URL + "/{oib}", EXISTING_OIB_1).exchange();

        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
        assertThat(repository.findByOib(EXISTING_OIB_1)).isEmpty();
    }

    @Test
    void deleteByOib_whenCardRequestDoesNotExist_returnsNoContent() {
        MvcTestResult result = mvc.delete().uri(BASE_URL + "/{oib}", UNUSED_VALID_OIB).exchange();

        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }
}