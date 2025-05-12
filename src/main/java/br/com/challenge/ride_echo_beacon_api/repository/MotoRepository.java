package br.com.challenge.ride_echo_beacon_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.challenge.ride_echo_beacon_api.model.Moto;

public interface MotoRepository extends JpaRepository<Moto, Long>, JpaSpecificationExecutor<Moto>{
}
