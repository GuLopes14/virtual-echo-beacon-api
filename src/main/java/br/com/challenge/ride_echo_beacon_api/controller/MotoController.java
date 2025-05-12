package br.com.challenge.ride_echo_beacon_api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.challenge.ride_echo_beacon_api.model.Moto;
import br.com.challenge.ride_echo_beacon_api.model.dto.EchoBeaconResponse;
import br.com.challenge.ride_echo_beacon_api.model.dto.MotoResponse;
import br.com.challenge.ride_echo_beacon_api.repository.EchoBeaconRepository;
import br.com.challenge.ride_echo_beacon_api.repository.MotoRepository;
import br.com.challenge.ride_echo_beacon_api.specification.MotoSpecification;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/motos")
public class MotoController {

        private final Logger log = LoggerFactory.getLogger(getClass());

        public record MotoFilter(String modelo, String placa) {
        }

        @Autowired
        private MotoRepository repository;

        @Autowired
        private EchoBeaconRepository echoBeaconRepository;

        @GetMapping
        @Cacheable("motos")
        public List<MotoResponse> index(MotoFilter filters) {
                log.info("Listando motos com filtros: modelo={}, placa={}", filters.modelo(), filters.placa());
                var motos = repository.findAll(MotoSpecification.withFilters(filters));

                return motos.stream()
                                .map(moto -> new MotoResponse(
                                                moto.getId(),
                                                moto.getPlaca(),
                                                moto.getChassi(),
                                                moto.getModelo(),
                                                moto.getProblema(),
                                                new EchoBeaconResponse(
                                                                moto.getEchoBeacon().getNumeroIdentificacao(),
                                                                moto.getEchoBeacon().getStatus()),
                                                moto.getDataRegistro()))
                                .toList();
        }

        @PostMapping
        @ResponseStatus(code = HttpStatus.CREATED)
        @CacheEvict(value = "motos", allEntries = true)
        public MotoResponse create(@RequestBody @Valid Moto moto) {
                log.info("Cadastrando moto " + moto.getModelo());

                if (moto.getEchoBeacon() != null && moto.getEchoBeacon().getId() != null) {
                        var echoBeacon = echoBeaconRepository.findById(moto.getEchoBeacon().getId())
                                        .orElseThrow(
                                                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                                        "EchoBeacon não encontrado"));
                        moto.setEchoBeacon(echoBeacon);
                }

                var motoSaved = repository.save(moto);

                return new MotoResponse(
                                motoSaved.getId(),
                                motoSaved.getPlaca(),
                                motoSaved.getChassi(),
                                motoSaved.getModelo(),
                                motoSaved.getProblema(),
                                new EchoBeaconResponse(
                                                motoSaved.getEchoBeacon().getNumeroIdentificacao(),
                                                motoSaved.getEchoBeacon().getStatus()),
                                motoSaved.getDataRegistro());
        }

        @GetMapping("{id}")
        public MotoResponse get(@PathVariable Long id) {
                log.info("Buscando moto " + id);
                Moto moto = getMoto(id);
                return new MotoResponse(
                                moto.getId(),
                                moto.getPlaca(),
                                moto.getChassi(),
                                moto.getModelo(),
                                moto.getProblema(),
                                new EchoBeaconResponse(
                                                moto.getEchoBeacon().getNumeroIdentificacao(),
                                                moto.getEchoBeacon().getStatus()),
                                moto.getDataRegistro());
        }

        @DeleteMapping("{id}")
        @CacheEvict(value = "motos", allEntries = true)
        public ResponseEntity<String> destroy(@PathVariable Long id) {
                log.info("Apagando moto " + id);
                Moto moto = getMoto(id);
                repository.delete(moto);
                log.info("Moto " + id + " apagada com sucesso");
                return ResponseEntity.ok("Moto com ID " + id + " foi deletada com sucesso.");
        }

        @PutMapping("{id}")
        @CacheEvict(value = "motos", allEntries = true)
        public MotoResponse update(@PathVariable Long id, @RequestBody @Valid Moto moto) {
                log.info("Atualizando moto " + id + " para " + moto);

                Moto existingMoto = getMoto(id);

                existingMoto.setPlaca(moto.getPlaca());
                existingMoto.setChassi(moto.getChassi());
                existingMoto.setModelo(moto.getModelo());
                existingMoto.setProblema(moto.getProblema());

                if (moto.getEchoBeacon() != null && moto.getEchoBeacon().getId() != null) {
                        var echoBeacon = echoBeaconRepository.findById(moto.getEchoBeacon().getId())
                                        .orElseThrow(
                                                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                                        "EchoBeacon não encontrado"));
                        existingMoto.setEchoBeacon(echoBeacon);
                }

                Moto updatedMoto = repository.save(existingMoto);

                return new MotoResponse(
                                updatedMoto.getId(),
                                updatedMoto.getPlaca(),
                                updatedMoto.getChassi(),
                                updatedMoto.getModelo(),
                                updatedMoto.getProblema(),
                                new EchoBeaconResponse(
                                                updatedMoto.getEchoBeacon().getNumeroIdentificacao(),
                                                updatedMoto.getEchoBeacon().getStatus()),
                                updatedMoto.getDataRegistro());
        }

        private Moto getMoto(Long id) {
                return repository
                                .findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Moto não encontrada"));
        }
}