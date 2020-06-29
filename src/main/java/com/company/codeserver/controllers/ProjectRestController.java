package com.company.codeserver.controllers;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import javax.validation.Valid;

import com.company.codeserver.entities.Project;
import com.company.codeserver.exceptions.FoundException;
import com.company.codeserver.exceptions.NotFoundException;
import com.company.codeserver.exceptions.SdlcConflictException;
import com.company.codeserver.services.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(ProjectRestController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "Project")
public class ProjectRestController {

  public static final String ENDPOINT = "/api/v2/projects";
  public static final String ENDPOINT_ID = "/{id}";
  public static final String PATH_VARIABLE_ID = "id";

  private static final String API_PARAM_ID = "ID";

  @Autowired
  private ProjectService projectService;
  private ObjectMapper objectMapper=new ObjectMapper();

  @ApiOperation("Get a Project")
  @GetMapping(ENDPOINT_ID)
  public ResponseEntity<Project> getProject(
      @ApiParam(name = API_PARAM_ID,
                required = true)
      @PathVariable(PATH_VARIABLE_ID)
      final long projectId) {
    try {
      Project project = projectService.getProject(projectId);
      return ResponseEntity.ok(project);
    } catch (NotFoundException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @ApiOperation("Create a Project")
  @PostMapping
  public ResponseEntity<Project> createProject(@Valid
  @RequestBody
      Project project) {
    try {
      if (project.getExternalId() == null || project.getSdlcSystem() == null)
        return ResponseEntity.badRequest().build();
      Project projectResp = projectService.createProject(project);
      final URI location =
          ServletUriComponentsBuilder.fromCurrentServletMapping().path(ENDPOINT + "/").build().expand(projectResp.getId()).toUri();
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(location);
      ResponseEntity<Project> entity = new ResponseEntity<Project>(headers, HttpStatus.CREATED);
      return entity;
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (FoundException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
      System.out.println("here");
      return ResponseEntity.badRequest().build();
    }

  }

  @ApiOperation("Update a Project")
  @PatchMapping(value = ENDPOINT_ID)
  public ResponseEntity<Project> updateProject(
      @ApiParam(name = API_PARAM_ID,
                required = true)
      @PathVariable(PATH_VARIABLE_ID)
      final long projectId,
  @RequestBody
      Project project) {
    try{
    Project updatedProject=projectService.updateProject(project,projectId);
    return ResponseEntity.ok(updatedProject);
  } catch(JsonProcessingException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  } catch(NotFoundException e) {
      return ResponseEntity.notFound().build();
    }
    catch (SdlcConflictException | FoundException e){
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

}
