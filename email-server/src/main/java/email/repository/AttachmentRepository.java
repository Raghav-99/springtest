package email.repository;

import org.springframework.data.repository.CrudRepository;
import email.entity.Attachment;

public interface AttachmentRepository extends CrudRepository<Attachment, Integer> {
}
