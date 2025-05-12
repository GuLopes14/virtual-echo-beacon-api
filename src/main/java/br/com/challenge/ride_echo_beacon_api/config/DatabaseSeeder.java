package br.com.challenge.ride_echo_beacon_api.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.challenge.ride_echo_beacon_api.model.EchoBeacon;
import br.com.challenge.ride_echo_beacon_api.model.Moto;
import br.com.challenge.ride_echo_beacon_api.model.enums.Modelo;
import br.com.challenge.ride_echo_beacon_api.model.enums.StatusAtivo;
import br.com.challenge.ride_echo_beacon_api.repository.EchoBeaconRepository;
import br.com.challenge.ride_echo_beacon_api.repository.MotoRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseSeeder {

    @Autowired
    private EchoBeaconRepository echoBeaconRepository;

    @Autowired
    private MotoRepository motoRepository;

    @PostConstruct
    public void init() {
        var echoBeacons = List.of(
            EchoBeacon.builder()
                .numeroIdentificacao(1)
                .status(StatusAtivo.ATIVO)
                .versaoFirmware("v1.0.0")
                .statusConexao("CONECTADO")
                .dataRegistro(LocalDate.now().minusDays(10))
                .build(),
            EchoBeacon.builder()
                .numeroIdentificacao(2)
                .status(StatusAtivo.DESATIVADO)
                .versaoFirmware("v1.0.0")
                .statusConexao("DESCONECTADO")
                .dataRegistro(LocalDate.now().minusDays(20))
                .build(),
            EchoBeacon.builder()
                .numeroIdentificacao(3)
                .status(StatusAtivo.ATIVO)
                .versaoFirmware("v1.0.0")
                .statusConexao("CONECTADO")
                .dataRegistro(LocalDate.now().minusDays(5))
                .build()
        );

        echoBeaconRepository.saveAll(echoBeacons);

        var motos = List.of(
            Moto.builder()
                .placa("ABC1234")
                .chassi("1HGCM82633A123456")
                .modelo(Modelo.MOTTU_POP)
                .problema("Sem problemas")
                .echoBeacon(echoBeacons.get(0)) 
                .dataRegistro(LocalDate.now().minusDays(5))
                .build(),
            Moto.builder()
                .placa("XYZ5678")
                .chassi("1HGCM82633A654321")
                .modelo(Modelo.MOTTU_SPORT)
                .problema("Problema no motor")
                .echoBeacon(echoBeacons.get(1)) 
                .dataRegistro(LocalDate.now().minusDays(15))
                .build(),
            Moto.builder()
                .placa("DEF8901")
                .chassi("1HGCM82633A789012")
                .modelo(Modelo.MOTTU_E)
                .problema("Freios desgastados")
                .echoBeacon(echoBeacons.get(2)) 
                .dataRegistro(LocalDate.now().minusDays(20))
                .build()
        );

        motoRepository.saveAll(motos);
    }
}