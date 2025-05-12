package br.com.challenge.ride_echo_beacon_api.model;

import java.time.LocalDate;

import br.com.challenge.ride_echo_beacon_api.model.enums.StatusAtivo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EchoBeacon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numeroIdentificacao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O campo 'status' é obrigatório")
    private StatusAtivo status;

    @NotBlank(message = "O campo 'versaoFirmware' é obrigatório")
    private String versaoFirmware;

    @NotBlank(message = "O campo 'statusConexao' é obrigatório")
    private String statusConexao;

    @NotBlank(message = "O campo 'dataRegistro' é obrigatório")
    @PastOrPresent(message = "O campo 'dataRegistro' deve ser uma data passada ou presente")
    private LocalDate dataRegistro;

}
