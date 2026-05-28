package springtest.email.repository;

import org.springframework.data.repository.CrudRepository;
import springtest.email.entity.Attachment;

public interface AttachmentRepository extends CrudRepository<Attachment, Integer> {
}
