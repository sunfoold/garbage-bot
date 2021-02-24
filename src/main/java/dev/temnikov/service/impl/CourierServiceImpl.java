package dev.temnikov.service.impl;

import dev.temnikov.service.CourierService;
import dev.temnikov.domain.Courier;
import dev.temnikov.repository.CourierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Courier}.
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    private final Logger log = LoggerFactory.getLogger(CourierServiceImpl.class);

    private final CourierRepository courierRepository;

    public CourierServiceImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    public Courier save(Courier courier) {
        log.debug("Request to save Courier : {}", courier);
        return courierRepository.save(courier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Courier> findAll() {
        log.debug("Request to get all Couriers");
        return courierRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Courier> findOne(Long id) {
        log.debug("Request to get Courier : {}", id);
        return courierRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Courier : {}", id);
        courierRepository.deleteById(id);
    }
}
