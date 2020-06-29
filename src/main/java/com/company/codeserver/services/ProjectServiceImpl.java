package com.company.codeserver.services;

import java.util.List;

import com.company.codeserver.entities.Project;
import com.company.codeserver.entities.SdlcSystem;
import com.company.codeserver.exceptions.FoundException;
import com.company.codeserver.exceptions.NotFoundException;
import com.company.codeserver.exceptions.SdlcConflictException;
import com.company.codeserver.repositories.ProjectRepository;
import com.company.codeserver.repositories.SdlcSystemRepository;
import com.company.codeserver.services.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private SdlcSystemRepository sdlcSystemRepository;

	@Autowired
	private ProjectRepository projectRepository;

	public Project getProject(long id) throws NotFoundException {
		return projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
	}

	@Override
	public Project createProject(Project project) throws NotFoundException, FoundException {
		long sdlcSystemId=project.getSdlcSystem().getId();
		sdlcSystemRepository.findById(sdlcSystemId).orElseThrow(()-> new NotFoundException(SdlcSystem.class,sdlcSystemId));
		if(projectRepository.findByExternalIdAndSdlcSystemId(project.getExternalId(),sdlcSystemId).isPresent()) {
			throw new FoundException(Project.class, project.getExternalId());
		}
		sdlcSystemRepository.findById(project.getSdlcSystem().getId()).ifPresent(project::setSdlcSystem);
		return projectRepository.save(project);
	}

	@Override
	public Project updateProject(Project projectToBePatched,long id)
			throws NotFoundException, JsonProcessingException, SdlcConflictException, FoundException {
		System.out.println("patch="+projectToBePatched);
		Project project=getProject(id);
		JSONObject req=new JSONObject(projectToBePatched);
		if(!req.has("externalId")) {
				projectToBePatched.setExternalId(project.getExternalId());
		}
		if(req.has("externalId") && req.has("sdlcSystem")){
			List<Project> projects=projectRepository.findByExternalId(projectToBePatched.getExternalId());
			for(Project proj: projects){
				if(proj.getExternalId().equalsIgnoreCase(projectToBePatched.getExternalId())
						&& proj.getSdlcSystem().getId()==projectToBePatched.getSdlcSystem().getId()){
					throw new FoundException(Project.class,projectToBePatched.getExternalId());
				}
			}

		}
		if(!req.has("name"))
			projectToBePatched.setName(project.getName());
		if(!req.has("sdlcSystem"))
			projectToBePatched.setSdlcSystem(project.getSdlcSystem());
		if(!req.has("createdDate"))
			projectToBePatched.setCreatedDate(project.getCreatedDate());
		if(req.has("sdlcSystem") && req.get("sdlcSystem")!=null){
			SdlcSystem sdlcSystem=sdlcSystemRepository.findById(projectToBePatched.getSdlcSystem().getId()).orElseThrow(()->
					 new NotFoundException(SdlcSystem.class,projectToBePatched.getSdlcSystem().getId()));
			projectToBePatched.setSdlcSystem(sdlcSystem);
		}
		return projectRepository.save(projectToBePatched);

	}

}
