package com.kanri.api.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public interface WorkLogResponseProjection {
    String getDescription();

    Instant getStartedAt();

    Double getStoryPointsSpent();

    @Value("#{target.user.email}")
    String getUserEmail();
}
