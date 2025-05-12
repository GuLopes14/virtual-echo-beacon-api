package br.com.challenge.ride_echo_beacon_api.repository;

import br.com.challenge.ride_echo_beacon_api.model.EchoBeacon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EchoBeaconRepository extends JpaRepository<EchoBeacon, Long>, JpaSpecificationExecutor<EchoBeacon> {
}