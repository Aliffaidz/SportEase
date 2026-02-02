package app.repositories;

import app.entities.Provision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvisionRepository extends JpaRepository<Provision,Integer> {
}
