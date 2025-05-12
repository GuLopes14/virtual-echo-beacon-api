package br.com.challenge.ride_echo_beacon_api.model.dto;

import br.com.challenge.ride_echo_beacon_api.model.enums.StatusAtivo;

public record EchoBeaconResponse (int numeroIdentificacao, StatusAtivo status){}
