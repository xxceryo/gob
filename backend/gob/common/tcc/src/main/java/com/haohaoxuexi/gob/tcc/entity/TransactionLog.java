package com.haohaoxuexi.gob.tcc.entity;

import com.haohaoxuexi.gob.datasource.domain.entity.BaseEntity;
import com.haohaoxuexi.gob.tcc.request.TccRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author loongzhou
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLog extends BaseEntity {

    /**
     * 事务ID
     */
    private String transactionId;

    /**
     * 业务场景
     */
    private String businessScene;

    /**
     * 业务模块
     */
    private String businessModule;

    /**
     * 状态
     */
    private TransActionLogState state;

    /**
     * Cancel的类型
     */
    private TransCancelSuccessType cancelType;

    /**
     * Try 方法的构造函数
     *
     * 因为 Try 都是需要往数据库中插入一条记录的，所以需要构造函数
     */
    public TransactionLog(TccRequest tccRequest, TransActionLogState state) {
        this.state = state;
        this.transactionId = tccRequest.getTransactionId();
        this.businessScene = tccRequest.getBusinessScene();
        this.businessModule = tccRequest.getBusinessModule();
    }

    /**
     * Cancel 操作的构造函数
     *
     * 因为会涉及到空回滚的情况，所以 Cancel 需要构造函数
     * Cancel 会的构造函数会多一个 cancelType 参数，用来表示 Cancel 的类型
     */
    public TransactionLog(TccRequest tccRequest, TransActionLogState state, TransCancelSuccessType cancelType) {
        this.state = state;
        this.transactionId = tccRequest.getTransactionId();
        this.businessScene = tccRequest.getBusinessScene();
        this.businessModule = tccRequest.getBusinessModule();
        this.cancelType = cancelType;
    }
}
