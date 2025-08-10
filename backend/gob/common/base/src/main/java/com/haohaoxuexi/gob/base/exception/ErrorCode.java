package com.haohaoxuexi.gob.base.exception;

/**
 * 错误码
 *
 * @author xxceryo
 */
public interface ErrorCode {
    /**
     * 错误码
     *
     * @return 错误码
     */
    String getCode();

    /**
     * 错误信息
     *
     * @return 错误信息
     */
    String getMessage();
}
