package com.lephix.easy.mvc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErrorResult extends JsonResult {

    private Integer httpCode;
}
