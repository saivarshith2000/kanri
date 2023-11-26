package com.kanri.api.mapper;

import com.kanri.api.dto.issue.AttachmentResponse;
import com.kanri.api.entity.Attachment;
import com.kanri.api.projection.AttachmentResponseProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentResponse projectToResponse(AttachmentResponseProjection projection);
    AttachmentResponse entityToResponse(Attachment attachment);
}
