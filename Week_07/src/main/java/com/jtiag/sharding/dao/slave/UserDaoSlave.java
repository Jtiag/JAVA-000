package com.jtiag.sharding.dao.slave;

import com.jtiag.sharding.domain.User;
import org.apache.ibatis.annotations.*;

/**
 * @author jasper
 */
@Mapper
public interface UserDaoSlave {
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Select("select * from t_user where id=#{id}")
    User findUserById(int id);
}
