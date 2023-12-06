package com.kanri.api.service;

import com.kanri.api.entity.Status;
import org.springframework.stereotype.Service;

@Service
public class StatusTransitionService {
    // TODO: Validate Status transition
    public static boolean isValid(Status from, Status to) {
        throw new UnsupportedOperationException("Not implemented yet");
        if (from == to) {
            return true;
        }
        return false;
    }
}
