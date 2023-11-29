package com.kanri.api.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public interface CommentResponseProjection {
    String getContent();

    Instant getCreatedAt();

    @Value("#{target.user.email}")
    String getUserEmail();
}
