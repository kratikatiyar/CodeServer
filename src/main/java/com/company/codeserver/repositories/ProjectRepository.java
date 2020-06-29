package com.company.codeserver.repositories;

import com.company.codeserver.entities.Project;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

    Optional<Project> findBySdlcSystemIdAndId(long sdlcSystemId, long projectId);


    @Query(value = "select * from project a  where a.external_id= ?1 and a.sdlc_system_id= ?2",nativeQuery = true)
    Optional<Project> findByExternalIdAndSdlcSystemId(String externalId, long id);

    @Query(value = "select * from project a  where a.external_id= ?1",nativeQuery = true)
    List<Project> findByExternalId(String externalId);

}
