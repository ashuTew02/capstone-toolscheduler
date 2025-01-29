package com.capstone.toolscheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.toolscheduler.model.Credential;
import com.capstone.toolscheduler.model.Credential.CredentialId;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, CredentialId> {
    // Additional custom queries if needed.
}
