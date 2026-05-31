package email.mapper;

import dto.Attachment;
import dto.Email;
import dto.User;
import email.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class EntityDtoMapper {
    public static Email toDto(email.entity.Email e) {
        if (e == null) return null;
        Email d = new Email();
        d.setId(e.getId());
        d.setBody(e.getBody());
        d.setSubject(e.getSubject());
        d.setSender(toDto(e.getSender()));
        if (e.getAttachment() != null) {
            d.setAttachment(e.getAttachment().stream().map(EntityDtoMapper::toDto).collect(Collectors.toList()));
        }
        if (e.getRecipients() != null) {
            d.setRecipients(e.getRecipients().stream().map(EntityDtoMapper::toDto).collect(Collectors.toList()));
        }
        return d;
    }

    public static User toDto(email.entity.User u) {
        if (u == null) return null;
        User du = new User();
        du.setId(u.getId());
        du.setEmail(u.getEmail());
        return du;
    }

    public static Attachment toDto(email.entity.Attachment a) {
        if (a == null) return null;
        Attachment da = new Attachment();
        da.setId(a.getId());
        da.setBlob(a.getBlob());
        return da;
    }
}
