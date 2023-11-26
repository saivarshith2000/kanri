package com.kanri.api.projection;

public interface AttachmentResponseProjection {
    String getName();
    String getType();
    long getSize();
    byte[] getContent();
}
