package com.haohaoxuexi.gob.tcc.response;

import com.haohaoxuexi.gob.tcc.entity.TransTrySuccessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author loongzhou
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransactionTryResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean success;

    private String errorCode;

    private String errorMsg;

    private TransTrySuccessType transTrySuccessType;

    public TransactionTryResponse(Boolean success, TransTrySuccessType transTrySuccessType) {
        this.success = success;
        this.transTrySuccessType = transTrySuccessType;
    }

    public TransactionTryResponse(Boolean success, String errorCode, String errorMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
