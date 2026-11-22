package com.devanshu.distributedlovable.workspaceservice.entity;

import java.io.Serial;
import java.time.Instant;

import com.devanshu.distributedlovable.commonlib.constants.ProjectMemberStatus;
import com.devanshu.distributedlovable.commonlib.constants.ProjectRole;
import com.devanshu.distributedlovable.commonlib.entity.CommonModel;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_member")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMember extends CommonModel {

	@Serial
	private static final long serialVersionUID = 7259382695086338430L;

	@EmbeddedId
	ProjectMemberId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("projectId")
	@JoinColumn(name = "projects_id", nullable = false)
	Projects projects;

	@Enumerated(EnumType.STRING)
	@Column(name = "project_role", nullable = false)
	ProjectRole projectRole;

	@Column(name = "invited_at", nullable = false)
	Instant invitedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "accepted_at", nullable = false)
	ProjectMemberStatus status;
}
