package com.devanshu.distributedlovable.workspaceservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devanshu.distributedlovable.commonlib.constants.ApplicationConstants;
import com.devanshu.distributedlovable.commonlib.exceptions.NotFoundException;
import com.devanshu.distributedlovable.workspaceservice.config.properties.StoragePropertiesConfig;
import com.devanshu.distributedlovable.workspaceservice.entity.ProjectFile;
import com.devanshu.distributedlovable.workspaceservice.entity.Projects;
import com.devanshu.distributedlovable.workspaceservice.repository.ProjectFileRepository;
import com.devanshu.distributedlovable.workspaceservice.repository.ProjectsRepository;
import com.devanshu.distributedlovable.workspaceservice.service.ProjectTemplateService;

import io.minio.CopyObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.SourceObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectTemplateServiceImpl implements ProjectTemplateService {

	private final MinioClient minioClient;
	private final ProjectFileRepository projectFileRepository;
	private final ProjectsRepository projectsRepository;
	private final StoragePropertiesConfig storagePropertiesConfig;

	@Override
	public void initializeProjectFromTemplate(final Long projectId) {
		Projects projects = projectsRepository.findById(projectId)
				.orElseThrow(() -> new NotFoundException("Project " + projectId.toString()));

		try {
			Iterable<Result<Item>> results = minioClient
					.listObjects(ListObjectsArgs.builder().bucket(ApplicationConstants.TEMPLATE_BUCKET)
							.prefix(ApplicationConstants.TEMPLATE_NAME + ApplicationConstants.FILE_SEPARATOR)
							.recursive(true).build());

			List<ProjectFile> filesToSave = new ArrayList<>(); // for metadata in postgres db

			for (Result<Item> result : results) {
				Item item = result.get();
				String sourceKey = item.objectName();

				String cleanPath = sourceKey
						.replaceFirst(ApplicationConstants.TEMPLATE_NAME + ApplicationConstants.FILE_SEPARATOR, "");
				String destKey = projectId + ApplicationConstants.FILE_SEPARATOR + cleanPath;

				minioClient
						.copyObject(CopyObjectArgs.builder().bucket(storagePropertiesConfig.getProjectBucket())
								.object(destKey).source(SourceObject.builder()
										.bucket(ApplicationConstants.TEMPLATE_BUCKET).object(sourceKey).build())
								.build());

				ProjectFile pf = ProjectFile.builder().projects(projects).path(cleanPath).minioObjectKey(destKey)
						.build();

				filesToSave.add(pf);
			}

			projectFileRepository.saveAll(filesToSave);

		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize project from template", e);
		}

	}
}