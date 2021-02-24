package dev.temnikov.service.impl;

import dev.temnikov.service.CourierCompanyService;
import dev.temnikov.domain.CourierCompany;
import dev.temnikov.repository.CourierCompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CourierCompany}.
 */
@Service
@Transactional
public class CourierCompanyServiceImpl implements CourierCompanyService {

    private final Logger log = LoggerFactory.getLogger(CourierCompanyServiceImpl.class);

    private final CourierCompanyRepository courierCompanyRepository;

    public CourierCompanyServiceImpl(CourierCompanyRepository courierCompanyRepository) {
        this.courierCompanyRepository = courierCompanyRepository;
    }

    @Override
    public CourierCompany save(CourierCompany courierCompany) {
        log.debug("Request to save CourierCompany : {}", courierCompany);
        return courierCompanyRepository.save(courierCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourierCompany> findAll() {
        log.debug("Request to get all CourierCompanies");
        return courierCompanyRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CourierCompany> findOne(Long id) {
        log.debug("Request to get CourierCompany : {}", id);
        return courierCompanyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourierCompany : {}", id);
        courierCompanyRepository.deleteById(id);
    }
}
