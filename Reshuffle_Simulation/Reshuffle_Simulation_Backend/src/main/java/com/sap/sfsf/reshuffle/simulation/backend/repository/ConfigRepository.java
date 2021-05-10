package com.sap.sfsf.reshuffle.simulation.backend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sap.sfsf.reshuffle.simulation.backend.model.Config;

@Repository
public interface ConfigRepository extends CrudRepository<Config, String> {
	
	@Query(nativeQuery = true, value = "select top 1 * from Config C")
	Config findOne();

}
