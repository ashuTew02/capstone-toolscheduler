package com.capstone.toolscheduler.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "credential")
@IdClass(Credential.CredentialId.class)
public class Credential {

    @Id
    @Column(name = "owner")
    private String owner;

    @Id
    @Column(name = "repo")
    private String repository;

    @Column(name = "pat")
    private String personalAccessToken;

    public Credential() {
    }

    public Credential(String owner, String repository, String personalAccessToken) {
        this.owner = owner;
        this.repository = repository;
        this.personalAccessToken = personalAccessToken;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getPersonalAccessToken() {
        return personalAccessToken;
    }

    public void setPersonalAccessToken(String personalAccessToken) {
        this.personalAccessToken = personalAccessToken;
    }

    public static class CredentialId implements Serializable {
        private String owner;
        private String repository;

        public CredentialId() {
        }

        public CredentialId(String owner, String repository) {
            this.owner = owner;
            this.repository = repository;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getRepository() {
            return repository;
        }

        public void setRepository(String repository) {
            this.repository = repository;
        }
    }
}
