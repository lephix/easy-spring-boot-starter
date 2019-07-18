package com.lephix.easy.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ConditionalOnProperty(prefix = "easy", name = "mvc.rewriteResponse", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication
@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class EasyResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ErrorResult) {
            ErrorResult result = (ErrorResult) body;
            response.setStatusCode(HttpStatus.valueOf(result.getHttpCode()));
            return result;
        } else if (body instanceof Page) {
            Page page = (Page) body;
            PageableResult result = new PageableResult();
            result.setData(page.getContent());
            result.setPage(page.getPageable().getPageNumber())
                    .setSize(page.getPageable().getPageSize())
                    .setTotal(page.getTotalElements());
            return result;
        } else if (body instanceof JsonResult) {
            return body;
        } else if (body instanceof String) {
            return body;
        } else {
            return new JsonResult().setData(body);
        }
    }
}
