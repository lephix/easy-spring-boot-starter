package com.lephix.easy.mvc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageableResult extends JsonResult {

    private int size;
    private int page;
    private long total;
}
