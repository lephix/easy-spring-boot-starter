package com.lephix.easy.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

@ConditionalOnProperty(prefix = "easy", name = "mvc.rewriteErrorResponse", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication
@RestController
@Slf4j
public class EasyErrorController implements ErrorController {

    @RequestMapping("/error")
    public ErrorResult error(HttpServletRequest request) {
        int httpCode = (int) request.getAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE);
        String errorMsg = (String) request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE);

        ErrorResult result = new ErrorResult().setHttpCode(httpCode);
        result.setSuccess(false).setMessage(errorMsg).setCode(httpCode);
        return result;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
