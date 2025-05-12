package br.com.challenge.ride_echo_beacon_api.specification;

import org.springframework.data.jpa.domain.Specification;

import br.com.challenge.ride_echo_beacon_api.controller.EchoBeaconController.EchoBeaconFilter;
import br.com.challenge.ride_echo_beacon_api.model.EchoBeacon;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;

public class EchoBeaconSpecification {
    public static Specification<EchoBeacon> withFilters(EchoBeaconFilter filters) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            if (filters.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filters.status()));
            }
            if (filters.versaoFirmware() != null) {
                predicates.add(criteriaBuilder.like(root.get("versaoFirmware"), "%" + filters.versaoFirmware() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}