package com.sap.sfsf.reshuffle.simulation.backend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sap.sfsf.reshuffle.simulation.backend.model.History;

@Repository
public interface HistoryRepository extends CrudRepository<History, String> {
	
	@Query(nativeQuery = true, value = "select top 1 * from History h order by h.checkedAt desc")
	History findLatesOne();

}
