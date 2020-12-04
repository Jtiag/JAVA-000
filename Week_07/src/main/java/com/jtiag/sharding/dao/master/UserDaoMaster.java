package com.jtiag.sharding.dao.master;

import com.jtiag.sharding.domain.User;
import org.apache.ibatis.annotations.*;

/**
 * @author jasper
 */
@Mapper
public interface UserDaoMaster {
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Select("select * from t_user where id=#{id}")
    User findUserById(int id);

    /**
     * 插入一个用户
     *
     * @param user
     */
    @Insert("insert into t_user(name,email,mobile,password,total_data_num,gmt_modify,gmt_created) values(#{name}," +
            "#{email},#{mobile},#{password},#{totalDataNum},#{gmtModify},gmtCreated)")
    void save(User user);

    /**
     * 根据id更新用户电话
     *
     * @param id
     * @param mobile
     */
    @Update("update t_user set mobile=#{mobile} where id=#{id}")
    void updateMobileById(int id, String mobile);

    /**
     * 根据id删除用户
     *
     * @param id
     */
    @Delete("delete from t_user where id=#{id}")
    void deleteById(int id);
}
