package com.kanri.api.projection;

import java.time.Instant;

public interface AttachmentResponseProjection {
    String getName();
    String getType();
    long getSize();
    byte[] getContent();
    Instant getCreatedAt();
}
