package com.company.codeserver.services;

import org.json.JSONObject;

import com.company.codeserver.entities.Project;
import com.company.codeserver.entities.Project;
import com.company.codeserver.exceptions.FoundException;
import com.company.codeserver.exceptions.NotFoundException;
import com.company.codeserver.exceptions.SdlcConflictException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProjectService {

    Project getProject(long id) throws NotFoundException;
    Project createProject(Project project) throws NotFoundException, FoundException;

  Project updateProject(Project project,long id)
      throws NotFoundException, JsonProcessingException, SdlcConflictException, FoundException;
}
