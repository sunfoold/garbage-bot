package dev.temnikov.service.impl;

import dev.temnikov.service.GarbageService;
import dev.temnikov.domain.Garbage;
import dev.temnikov.repository.GarbageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Garbage}.
 */
@Service
@Transactional
public class GarbageServiceImpl implements GarbageService {

    private final Logger log = LoggerFactory.getLogger(GarbageServiceImpl.class);

    private final GarbageRepository garbageRepository;

    public GarbageServiceImpl(GarbageRepository garbageRepository) {
        this.garbageRepository = garbageRepository;
    }

    @Override
    public Garbage save(Garbage garbage) {
        log.debug("Request to save Garbage : {}", garbage);
        return garbageRepository.save(garbage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Garbage> findAll() {
        log.debug("Request to get all Garbage");
        return garbageRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Garbage> findOne(Long id) {
        log.debug("Request to get Garbage : {}", id);
        return garbageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Garbage : {}", id);
        garbageRepository.deleteById(id);
    }
}
