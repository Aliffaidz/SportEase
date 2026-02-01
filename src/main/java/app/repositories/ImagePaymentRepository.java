package app.repositories;

import app.entities.ImagePaymentVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagePaymentRepository extends JpaRepository<ImagePaymentVerification,String> {
}
