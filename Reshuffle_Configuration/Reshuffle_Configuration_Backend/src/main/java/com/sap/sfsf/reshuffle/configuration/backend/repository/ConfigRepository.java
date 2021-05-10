package com.sap.sfsf.reshuffle.configuration.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sap.sfsf.reshuffle.configuration.backend.model.Config;

@Repository
public interface ConfigRepository extends CrudRepository<Config, Integer>{}
