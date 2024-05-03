
package com.carlease.project.autosuggestor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutosuggestorRepository extends JpaRepository<Autosuggestor, Long> {
    Autosuggestor findByApplicationId(long id);
}