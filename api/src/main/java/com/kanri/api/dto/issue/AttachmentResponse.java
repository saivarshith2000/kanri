package com.kanri.api.dto.issue;

import lombok.Data;

@Data
public class AttachmentResponse {
    private String name;
    private String type;
    private Long size;
    private byte[] content;
}
