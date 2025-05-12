package br.com.challenge.ride_echo_beacon_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

import br.com.challenge.ride_echo_beacon_api.model.EchoBeacon;
import br.com.challenge.ride_echo_beacon_api.repository.EchoBeaconRepository;
import br.com.challenge.ride_echo_beacon_api.specification.EchoBeaconSpecification;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/echo-beacons")
public class EchoBeaconController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private EchoBeaconRepository repository;

    public record EchoBeaconFilter(String status, String versaoFirmware) {
    }

    @GetMapping
    @Cacheable("echoBeacons")
    public Page<EchoBeacon> index(EchoBeaconFilter filters,
            @PageableDefault(size = 10, sort = "numeroIdentificacao", direction = Direction.ASC) Pageable pageable) {
        log.info("Listando EchoBeacons com filtros e paginação");
        var specification = EchoBeaconSpecification.withFilters(filters);
        return repository.findAll(specification, pageable);
    }

    
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @CacheEvict(value = "echoBeacons", allEntries = true)
    public ResponseEntity<EchoBeacon> create(@RequestBody @Valid EchoBeacon echoBeacon) {
        log.info("Cadastrando EchoBeacon com número de identificação " + echoBeacon.getNumeroIdentificacao());
        repository.save(echoBeacon);
        return ResponseEntity.status(201).body(echoBeacon);
    }


    @GetMapping("{id}")
    public EchoBeacon get(@PathVariable Long id) {
        log.info("Buscando EchoBeacon " + id);
        return getBeacon(id);
    }

    @PutMapping("{id}")
    @CacheEvict(value = "echoBeacons", allEntries = true)
    public EchoBeacon update(@PathVariable Long id, @RequestBody @Valid EchoBeacon echoBeacon) {
        log.info("Atualizando EchoBeacon " + id + " para " + echoBeacon);

        echoBeacon.setId(id);
        return repository.save(echoBeacon);
    }
    
    @DeleteMapping("{id}")
    @CacheEvict(value = "echoBeacons", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> destroy(@PathVariable Long id) {
        log.info("Apagando EchoBeacon " + id);
        repository.deleteById(id);
        return ResponseEntity.ok("Echo Beacon com o ID " + id + " foi deletado com sucesso");
    }

    private EchoBeacon getBeacon(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada"));
    }
}
