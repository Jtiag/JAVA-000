
package com.jtiag.trans.account.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The type Account dto.
 *
 * @author xiaoyu
 */
@Data
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 7223470850578998427L;
    
    /**
     * 用户id
     */
    private String userId;

    /**
     * 扣款金额
     */
    private BigDecimal amount;

}
