package com.jtiag.trans.account.mapper;

import com.jtiag.trans.account.dto.AccountDTO;
import com.jtiag.trans.account.entity.AccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author jasper
 */
@Mapper
public interface AccountMapper {
    
    /**
     * update
     *
     * @param accountDTO the account dto
     * @return the int
     */
    @Update("update account set balance = balance - #{amount}," +
            " freeze_amount= freeze_amount + #{amount} ,update_time = now()" +
            " where user_id =#{userId}  and  balance > 0  ")
    int update(AccountDTO accountDTO);
    
    /**
     * Test update int.
     *
     * @param accountDTO the account dto
     * @return the int
     */
    @Update("update account set balance = balance - #{amount}, update_time = now() " +
            " where user_id =#{userId}  and  balance > 0  ")
    int testUpdate(AccountDTO accountDTO);
    
    /**
     * Confirm int.
     *
     * @param accountDTO the account dto
     * @return the int
     */
    @Update("update account set " +
            " freeze_amount= freeze_amount - #{amount}" +
            " where user_id =#{userId}  and freeze_amount >0 ")
    int confirm(AccountDTO accountDTO);
    
    /**
     * Cancel int.
     *
     * @param accountDTO the account dto
     * @return the int
     */
    @Update("update account set balance = balance + #{amount}," +
            " freeze_amount= freeze_amount -  #{amount} " +
            " where user_id =#{userId}  and freeze_amount >0")
    int cancel(AccountDTO accountDTO);
    
    /**
     * 根据userId获取用户账户信息
     *
     * @param userId 用户id
     * @return AccountDO account do
     */
    @Select("select id,user_id,balance, freeze_amount from account where user_id =#{userId} limit 1")
    AccountDO findByUserId(String userId);
}
