package security.instagram.service;

import security.instagram.entity.Document;
import security.instagram.entity.User;
import security.instagram.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static security.instagram.entity.enums.Visibility.*;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final GroupMemberRepository groupMembers;

    public boolean canView(Document doc, User requester) {
        if (doc.getIsDeleted() != null && doc.getIsDeleted()) return false;
        switch (doc.getVisibility()) {
            case PUBLIC:
                return true;
            case PRIVATE:
                return doc.getOwner().getId().equals(requester.getId());
            case GROUP:
                return doc.getGroup() != null &&
                        groupMembers.existsByGroupAndUser(doc.getGroup(), requester.getId());
            default:
                return false;
        }
    }

    public boolean canModify(Document doc, User requester) {
        return doc.getOwner().getId().equals(requester.getId());
    }
}