package com.kanri.api.projection;

import org.springframework.beans.factory.annotation.Value;

public interface CommentResponseProjection {
    String getContent();

    String getCreatedAt();

    @Value("#{target.user.email}")
    String getUserEmail();
}
