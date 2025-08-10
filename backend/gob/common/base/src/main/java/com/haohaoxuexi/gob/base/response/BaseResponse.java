package com.haohaoxuexi.gob.base.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 通用出参
 *
 * @author xxceryo
 */
@Setter
@Getter
@ToString
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean success;

    private String responseCode;

    private String responseMessage;
}
