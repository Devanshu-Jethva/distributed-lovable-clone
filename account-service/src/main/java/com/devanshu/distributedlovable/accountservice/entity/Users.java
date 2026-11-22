package com.devanshu.distributedlovable.accountservice.entity;

import java.io.Serial;
import java.time.Instant;

import com.devanshu.distributedlovable.commonlib.entity.CommonModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users extends CommonModel {

	@Serial
	private static final long serialVersionUID = -2264536331038896888L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "name", nullable = false)
	String name;

	@Column(name = "username", unique = true, nullable = false)
	String username;

	@Column(name = "password", nullable = false)
	String password;

	@Column(name = "stripe_customer_id", unique = true)
	String stripeCustomerId;

	@Column(name = "deleted_at")
	Instant deletedAt;
}
