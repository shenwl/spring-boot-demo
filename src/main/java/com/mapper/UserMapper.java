package com.mapper;

import com.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findUserById(@Param("id") Integer id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Select("INSERT INTO user (username, encrypted_password, avatar, created_at, updated_at) " +
            "VALUES (#{username}, #{encryptedPassword}, #{avatar}, now(), now())")
    User insertOne(
            @Param("username") String username,
            @Param("encryptedPassword") String encryptedPassword,
            @Param("avatar") String avatar
    );

    @Select("UPDATE user SET username=#{username}, avatar=#{avatar}, updated_at=now() WHERE id=#{id}")
    User updateOne(
            @Param("id") Integer id,
            @Param("username") String username,
            @Param("avatar") String avatar
    );
}
