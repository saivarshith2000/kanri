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

/**
 * This aspect is used in controllers which use project code in their routes
 * but don't necessarily use it in their business logic. Such Controllers should be
 * annotated with the @ProjectExists annotation and the project code parameter in
 * their routes should also be annotated with @ProjectExists.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CheckProjectExistsAspect {
    private final ProjectRepository projectRepository;

    @Before("@annotation(com.kanri.api.annotation.ProjectExists)")
    public void process(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Annotation[][] annotationMatrix = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < annotationMatrix.length; i++) {
            Annotation[] annotations = annotationMatrix[i];
            for (Annotation annotation: annotations) {
                if (annotation.annotationType() == ProjectExists.class) {
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
