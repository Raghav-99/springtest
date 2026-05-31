package email.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import email.entity.Attachment;
import email.repository.AttachmentRepository;
import email.service.AttachmentService;

public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    @Autowired
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }
    @Override
    public Attachment saveAttachment(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }
}
