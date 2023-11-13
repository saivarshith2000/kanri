package com.kanri.api.aspect;

import com.kanri.api.annotation.ProjectExists;
import com.kanri.api.exception.NotFoundException;
import com.kanri.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
//@RequiredArgsConstructor
@Slf4j
public class CheckProjectExistsAspect {
    private final ProjectRepository projectRepository;

    public CheckProjectExistsAspect(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
        log.info("Initialized");
    }

    @Before("@annotation(com.kanri.api.annotation.ProjectExists)")
    public void process(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Annotation[][] annotationMatrix = signature.getMethod().getParameterAnnotations();
        log.info("Aspect called");
        for (int i = 0; i < annotationMatrix.length; i++) {
            Annotation[] annotations = annotationMatrix[i];
            for (Annotation annotation: annotations) {
                if (annotation.annotationType() == ProjectExists.class) {
                    log.info("Called for project " + (String) args[i]);
                    throwIfProjectDoesNotExist((String)args[i]);
                }
            }
        }
    }

    private void throwIfProjectDoesNotExist(String projectCode) {
        projectRepository.
                findByCode(projectCode)
                .orElseThrow(() -> new NotFoundException("Project " + projectCode + " doesn't exist"));
    }
}
