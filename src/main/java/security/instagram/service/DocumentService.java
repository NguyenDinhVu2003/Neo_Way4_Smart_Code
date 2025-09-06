package security.instagram.service;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.instagram.dto.document.DocumentCreateRequest;
import security.instagram.dto.document.DocumentResponse;
import security.instagram.dto.document.DocumentUpdateRequest;
import security.instagram.entity.Document;
import security.instagram.entity.Group;
import security.instagram.entity.Tag;
import security.instagram.entity.User;
import security.instagram.entity.enums.AiSummaryStatus;
import security.instagram.entity.enums.FileType;
import security.instagram.entity.enums.Visibility;
import security.instagram.repository.DocumentRepository;
import security.instagram.repository.GroupRepository;
import security.instagram.repository.TagRepository;
import security.instagram.utils.exception.ApiException;
import security.instagram.utils.exception.ErrorCode;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documents;
    private final GroupRepository groups;
    private final TagRepository tags;
    private final PermissionService perms;

    @Transactional
    public DocumentResponse create(DocumentCreateRequest req, User owner) {
        validateFile(req.getFileType(), req.getFileSizeBytes());
        Visibility vis = Visibility.valueOf(req.getVisibility().toUpperCase());
        Group group = null;
        if (vis == Visibility.GROUP) {
            if (StringUtils.isBlank(req.getGroupId())) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, "groupId is required for GROUP visibility");
            }
            Long gid;
            try { gid = Long.parseLong(req.getGroupId()); } catch (NumberFormatException ex) { throw new ApiException(ErrorCode.VALIDATION_ERROR, "groupId must be a number"); }
            group = groups.findById(gid)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Group not found"));
        }
        Set<Tag> tagEntities = new HashSet<>();
        if (req.getTags() != null) {
            for (String name : req.getTags()) {
                if (StringUtils.isBlank(name)) continue;
                String norm = name.trim().toLowerCase();
                Tag tag = tags.findByName(norm).orElseGet(() -> tags.save(Tag.builder().name(norm).build()));
                tagEntities.add(tag);
            }
        }
        FileType ft;
        switch (req.getFileType().toLowerCase()) {
            case "pdf":
                ft = FileType.PDF;
                break;
            case "doc":
            case "docx":
                ft = FileType.DOC;
                break;
            case "image":
            case "png":
            case "jpg":
            case "jpeg":
            case "gif":
                ft = FileType.IMAGE;
                break;
            default:
                throw new ApiException(ErrorCode.VALIDATION_ERROR, "fileType is not allowed");
        }
        Document d = Document.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .summary(StringUtils.abbreviate(req.getSummary(), 4000))
                .fileKey(req.getFileKey())
                .fileType(ft)
                .fileSizeBytes(req.getFileSizeBytes())
                .visibility(vis)
                .group(group.getId())
                .owner(owner)
                .aiSummaryStatus(req.getAi() != null && req.getAi().isAutoSummarize() ? AiSummaryStatus.PENDING : null)
                .tags(tagEntities)
                .build();
        documents.save(d);
        return HomeService.Mapper.toDocResponse(d);
    }

    @Transactional(readOnly = true)
    public DocumentResponse get(Long id, User me) {
        Document d = documents.findById(id).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Document not found"));
        if (!perms.canView(d, me)) throw new ApiException(ErrorCode.FORBIDDEN, "No access");
        return HomeService.Mapper.toDocResponse(d);

    }

    @Transactional
    public DocumentResponse update(Long id, DocumentUpdateRequest req, User me) {
        Document d = documents.findById(id).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Document not found"));
        if (!perms.canModify(d, me)) throw new ApiException(ErrorCode.FORBIDDEN, "Only owner can modify");
        if (req.getTitle() != null) d.setTitle(StringUtils.abbreviate(req.getTitle(), 256));
        if (req.getSummary() != null) d.setSummary(StringUtils.abbreviate(req.getSummary(), 4000));
        if (req.getDescription() != null) d.setDescription(StringUtils.abbreviate(req.getDescription(), 4000));
        if (req.getVisibility() != null) {
            Visibility vis = Visibility.valueOf(req.getVisibility().toUpperCase());
            d.setVisibility(vis);
            if (vis == Visibility.GROUP) {
                if (StringUtils.isBlank(req.getGroupId())) throw new ApiException(ErrorCode.VALIDATION_ERROR, "groupId required");
                Long gid;
                try { gid = Long.parseLong(req.getGroupId()); } catch (NumberFormatException ex) { throw new ApiException(ErrorCode.VALIDATION_ERROR, "groupId must be a number"); }
                Group g = groups.findById(gid)
                        .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Group not found"));
                d.setGroup(g.getId());
            } else {
                d.setGroup(null);
            }
        }
        if (req.getTags() != null) {
            Set<Tag> tagEntities = new HashSet<>();
            for (String name : req.getTags()) {
                if (StringUtils.isBlank(name)) continue;
                String norm = name.trim().toLowerCase();
                Tag tag = tags.findByName(norm).orElseGet(() -> tags.save(Tag.builder().name(norm).build()));
                tagEntities.add(tag);
            }
            d.setTags(tagEntities);
        }
        return HomeService.Mapper.toDocResponse(d);
    }

    private void validateFile(String type, long size) {
        if (size > 10 * 1024 * 1024L) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, "File must be ≤ 10MB");
        }
        String t = type.toLowerCase();
        if (!(t.equals("pdf") || t.equals("doc") || t.equals("docx")
                || t.equals("image") || t.equals("png") || t.equals("jpg") || t.equals("jpeg") || t.equals("gif"))) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, "fileType is not allowed");
        }
    }
}