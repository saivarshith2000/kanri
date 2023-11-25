package com.kanri.api.mapper;

import com.kanri.api.dto.issue.CommentResponse;
import com.kanri.api.entity.Comment;
import com.kanri.api.projection.CommentResponseProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponse entityToResponse(Comment comment);
    CommentResponse projectionToResponse(CommentResponseProjection comment);
}
