package hr.rba.rba_it.model;

import hr.rba.rba_it.enumeration.CardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "card_request")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "oib", nullable = false)
    private String oib;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CardStatus status;

}
