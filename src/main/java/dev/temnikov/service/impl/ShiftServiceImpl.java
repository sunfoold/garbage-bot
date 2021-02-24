package dev.temnikov.service.impl;

import dev.temnikov.service.ShiftService;
import dev.temnikov.domain.Shift;
import dev.temnikov.repository.ShiftRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Shift}.
 */
@Service
@Transactional
public class ShiftServiceImpl implements ShiftService {

    private final Logger log = LoggerFactory.getLogger(ShiftServiceImpl.class);

    private final ShiftRepository shiftRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Shift save(Shift shift) {
        log.debug("Request to save Shift : {}", shift);
        return shiftRepository.save(shift);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shift> findAll() {
        log.debug("Request to get all Shifts");
        return shiftRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Shift> findOne(Long id) {
        log.debug("Request to get Shift : {}", id);
        return shiftRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Shift : {}", id);
        shiftRepository.deleteById(id);
    }
}
