package com.devanshu.distributedlovable.workspaceservice.entity;

import java.io.Serial;
import java.time.Instant;

import com.devanshu.distributedlovable.commonlib.constants.PreviewStatus;
import com.devanshu.distributedlovable.commonlib.entity.CommonModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "preview")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Preview extends CommonModel {

	@Serial
	private static final long serialVersionUID = 3288748332619495494L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne
	@JoinColumn(name = "projects_id")
	Projects projects;

	@Column(name = "namespace", nullable = false)
	String namespace;

	@Column(name = "pod_name", nullable = false)
	String podName;

	@Column(name = "preview_url", nullable = false)
	String previewUrl;

	@Column(name = "started_at", nullable = false)
	Instant startedAt;

	@Column(name = "terminated_at")
	Instant terminatedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	PreviewStatus status;

}
