package com.haohaoxuexi.gob.tcc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haohaoxuexi.gob.tcc.entity.*;
import com.haohaoxuexi.gob.tcc.mapper.TransactionLogMapper;
import com.haohaoxuexi.gob.tcc.request.TccRequest;
import com.haohaoxuexi.gob.tcc.response.TransactionCancelResponse;
import com.haohaoxuexi.gob.tcc.response.TransactionConfirmResponse;
import com.haohaoxuexi.gob.tcc.response.TransactionTryResponse;

/**
 * 盲盒服务
 *
 * @author loongzhou
 */
public class TransactionLogService extends ServiceImpl<TransactionLogMapper, TransactionLog> {

    /**
     * TCC事务的Try
     *
     * @param tccRequest
     * @return
     */
    public TransactionTryResponse tryTransaction(TccRequest tccRequest) {
        //查询数据库中对应的事务 id 是否有对应的记录
        TransactionLog existTransactionLog = getExistTransLog(tccRequest);
        if (existTransactionLog == null) {//对应的事务 id 数据库没有记录
            //直接插入一条事务记录，状态为Try
            TransactionLog transactionLog = new TransactionLog(tccRequest, TransActionLogState.TRY);
            if (this.save(transactionLog)) {
                return new TransactionTryResponse(true, TransTrySuccessType.TRY_SUCCESS);
            }
            return new TransactionTryResponse(false, "TRY_FAILED", "TRY_FAILED");
        }

        //幂等
        //如果 Try 操作，查询对应的事务 id 在数据表已经有记录了，无论是 Try、Confirm、Cancel，都会直接返回 Try 已经执行过
        return new TransactionTryResponse(true, TransTrySuccessType.DUPLICATED_TRY);
    }

    /**
     * TCC事务的Confirm
     *
     * @param tccRequest
     * @return
     */
    public TransactionConfirmResponse confirmTransaction(TccRequest tccRequest) {
        TransactionLog existTransactionLog = getExistTransLog(tccRequest);
        if (existTransactionLog == null) {
            //如果对应的事务 id 数据库没有记录，说明该之前的 Try 没有执行，直接返回异常
            throw new UnsupportedOperationException("transaction can not confirm");
        }

        if (existTransactionLog.getState() == TransActionLogState.TRY) {//数据库中已经存在了一条 Try 记录
            //更新状态为 Confirm
            existTransactionLog.setState(TransActionLogState.CONFIRM);
            if (this.updateById(existTransactionLog)) {
                return new TransactionConfirmResponse(true, TransConfirmSuccessType.CONFIRM_SUCCESS);
            }
            //数据库更新失败，则返回 Confirm 失败
            return new TransactionConfirmResponse(false, "CONFIRM_FAILED", "CONFIRM_FAILED");
        }

        //幂等
        //如果数据库中，对应的事务 id 已经有 Confirm 记录了，直接返回 Confirm 已经执行过
        //此时返回 confirm 也是成功，但是成功的类型为 DuplicatedConfirm，之后会根据这个类型对于扣减库存的操作幂等处理
        if (existTransactionLog.getState() == TransActionLogState.CONFIRM) {
            return new TransactionConfirmResponse(true, TransConfirmSuccessType.DUPLICATED_CONFIRM);
        }

        //如果数据库中，对应的事务 id 是其他的状态，比如 Cancel ，直接返回无法进行 Confirm 操作
        throw new UnsupportedOperationException("transacton can not confirm :" + existTransactionLog.getState());
    }

    /**
     * TCC事务的Cancel
     *
     * @param tccRequest
     * @return
     */
    public TransactionCancelResponse cancelTransaction(TccRequest tccRequest) {
        //查询数据库中对应的事务 id 是否有对应的记录
        TransactionLog existTransactionLog = getExistTransLog(tccRequest);
        if (existTransactionLog == null) {//该事务 id 数据库没有记录，则说明这一次是空回滚
            //则数据库中直接记录一条状态为 Cancel的数据，并且取消类型为空回滚
            //这里插入一条数据，是为了后面出现事务悬挂。如果没有这条记录，后续的 Try 无法判断在它之前有无 Cancel 操作
            TransactionLog transactionLog = new TransactionLog(tccRequest, TransActionLogState.CANCEL, TransCancelSuccessType.EMPTY_CANCEL);
            if (this.save(transactionLog)) {
                //返回空回滚成功
                return new TransactionCancelResponse(true, TransCancelSuccessType.EMPTY_CANCEL);
            }
            return new TransactionCancelResponse(false, "EMPTY_CANCEL_FAILED", "EMPTY_CANCEL_FAILED");
        }

        if (existTransactionLog.getState() == TransActionLogState.TRY) {//对应事务 id 已经存在Try记录
            // 则直接更新状态为 Cancel，并且取消类型为 Try 成功后 Cancel
            existTransactionLog.setState(TransActionLogState.CANCEL);
            existTransactionLog.setCancelType(TransCancelSuccessType.CANCEL_AFTER_TRY_SUCCESS);
            if (this.updateById(existTransactionLog)) {
                return new TransactionCancelResponse(true, TransCancelSuccessType.CANCEL_AFTER_TRY_SUCCESS);
            }
            return new TransactionCancelResponse(false, "CANCEL_FAILED", "CANCEL_FAILED");
        }

        if (existTransactionLog.getState() == TransActionLogState.CONFIRM) {//对应事务 id 已经存在Confirm记录
            // 则直接更新状态为 Cancel，并且取消类型为 Confirm 成功后 Cancel
            existTransactionLog.setState(TransActionLogState.CANCEL);
            existTransactionLog.setCancelType(TransCancelSuccessType.CANCEL_AFTER_CONFIRM_SUCCESS);
            if (this.updateById(existTransactionLog)) {
                return new TransactionCancelResponse(true, TransCancelSuccessType.CANCEL_AFTER_CONFIRM_SUCCESS);
            }
            return new TransactionCancelResponse(false, "CANCEL_FAILED", "CANCEL_FAILED");
        }

        //幂等
        if (existTransactionLog.getState() == TransActionLogState.CANCEL) {//对应事务 id 已经存在Cancel记录
            // 则直接返回 Cancel 已经执行过
            return new TransactionCancelResponse(true, TransCancelSuccessType.DUPLICATED_CANCEL);
        }

        return new TransactionCancelResponse(false, "CANCEL_FAILED", "CANCEL_FAILED");
    }

    private TransactionLog getExistTransLog(TccRequest request) {
        QueryWrapper<TransactionLog> queryWrapper = new QueryWrapper<TransactionLog>();
        queryWrapper.eq("transaction_id", request.getTransactionId());
        queryWrapper.eq("business_scene", request.getBusinessScene());
        queryWrapper.eq("business_module", request.getBusinessModule());

        return this.getOne(queryWrapper);
    }

}
