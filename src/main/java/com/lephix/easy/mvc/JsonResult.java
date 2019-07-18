package com.lephix.easy.mvc;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class JsonResult {

    private Object data;
    private boolean success = true;
    private String message;
    private int code;

}
