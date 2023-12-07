package com.learning.dao;

import com.learning.dao.mapper.UserMapper;
import com.learning.entity.User;
import com.learning.util.paginated.PaginatedListHelper;
import com.learning.util.paginated.SimplePaginatedList;
import com.learning.web.user.UsersForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    public SimplePaginatedList getUsers(UsersForm form){
        String sql = "select count(distinct u.user_id) from users u";
        String filt = "";
        if (StringUtils.isNotBlank(form.getUsername()))
            filt += " where u.username LIKE '%" + form.getUsername() + "%'";
        sql = sql + filt;
        int total = -1;
        if (form.getPageSize() != -1) {
            total = getJdbcTemplate().queryForObject(sql, Integer.class);
            form.fixPageNumber(total);
        }

        StringBuilder sql2 = new StringBuilder();

        sql2.append("select * from users u " + filt);

        if (form.getPageSize() > 0) {
            sql2.append(" \nlimit ");
            if (form.getFirstResult() > 0)
                sql2.append(form.getFirstResult())
                        .append(", ");
            sql2.append(form.getPageSize());
        }

        List<User> list = getJdbcTemplate().query(sql2.toString(), new UserMapper());
        if (total == -1)
            total = list.size();
        return PaginatedListHelper.getPaginatedList(list, total, form);
    }

    public User getUserByUserId(Integer userId) {
        UserMapper usermapper = new UserMapper();
        String sql = "select * from users where user_id = ?";
        List<User> list = getJdbcTemplate().query(sql, usermapper, new Object[]{userId});
        if (list.isEmpty()){
            return null;
        }else {
            return (User)list.get(0);
        }
    }

    public User getUserByEmail(String email){
        UserMapper usermapper = new UserMapper();
        String sql = "select * from users where email = ?";
        List<User> list = getJdbcTemplate().query(sql, usermapper, new Object[]{email});
        if (list.isEmpty())
            return null;
        else
            return (User) list.get(0);
    }

    public void saveOrUpdate(final User user) {
        if(user!=null && user.getUsername()!=null && user.getPassword()!=null ) {
            if(user.getUserId() > 0) {
                String update = "update users set" +
                        " username = ?, password = ?, email = ?, role = ?, enabled = ? " +
                        " where user_id = ?";
                getJdbcTemplate().update(update, new Object[]{
                                user.getUsername(),
                                user.getPassword(),
                                user.getEmail(),
                                user.getUserRole(),
                                user.isEnabled(),
                                user.getUserId()
                    }
                );
            } else {
                String insert = "insert into users (" +
                        " username, password , email , role , enabled ) " +
                        " VALUES('"+user.getUsername()+"', '"
                        +user.getPassword()+"','"
                        +user.getEmail()+"','"
                        +user.getUserRole()+"',"
                        +user.isEnabled()+")";

                KeyHolder keyHolder = new GeneratedKeyHolder();
                getJdbcTemplate().update(
                        con -> {
                            PreparedStatement pst = con.prepareStatement(insert,new String[]{"user_id"});
                            return pst;
                        },
                        keyHolder
                );
                user.setUserId(keyHolder.getKey().intValue());
            }
        }
    }

    public void deleteUser(Integer userId) {
        String sql = "delete from users where user_id = ?";
        getJdbcTemplate().update(sql, new Object[]{userId});
    }

    public boolean isUniqueUser(User user) {
        String sql = "select count(*) from users where username = ? and user_id <> ?";
        Integer count = getJdbcTemplate().queryForObject(sql, Integer.class, new Object[]{user.getUsername(), user.getUserId()});
        return count.equals(0);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username))
            throw new UsernameNotFoundException("Username is blank, user not found");

        String sql = "select * from users where username = ?";
        User user;
        List<User> list = getJdbcTemplate().query(sql, new UserMapper(), new Object[]{username});
        if (list.isEmpty()){
            user = null;
        }else {
            user = (User)list.get(0);
        }


        if (user == null)
            throw new UsernameNotFoundException("User not found with username " + username);

        return user;
    }
}
