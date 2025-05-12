package br.com.challenge.ride_echo_beacon_api.model.dto;

import java.time.LocalDate;

import br.com.challenge.ride_echo_beacon_api.model.enums.Modelo;

public record MotoResponse(
        Long id,
        String placa,
        String chassi,
        Modelo modelo,
        String problema,
        EchoBeaconResponse echoBeacon,
        LocalDate dataRegistro) {
}
