package com.example.app.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "password_reset_token")
public class PasswordResetEntitiy implements Serializable {

	private static final long serialVersionUID = -1882771011155603438L;

	@Id
	@GeneratedValue
	private long id;

	private String token;

	@OneToOne
	@JoinColumn(name = "userId")
	private UserEntity entity;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserEntity getEntity() {
		return entity;
	}

	public void setEntity(UserEntity entity) {
		this.entity = entity;
	}
}
