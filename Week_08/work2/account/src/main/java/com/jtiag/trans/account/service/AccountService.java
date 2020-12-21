package com.jtiag.trans.account.service;
import com.jtiag.trans.account.dto.AccountDTO;
import com.jtiag.trans.account.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jasper 2020/12/18 下午4:47
 * @version 1.0.0
 * @desc
 */
@Service("accountService")
public class AccountService {
    @Autowired
    private AccountMapper accountMapper;

    public boolean payment(AccountDTO accountDTO) {
        return accountMapper.update(accountDTO) > 0;
    }
}
