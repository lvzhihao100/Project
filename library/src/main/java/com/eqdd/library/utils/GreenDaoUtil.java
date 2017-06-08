package com.eqdd.library.utils;

import com.eqdd.common.utils.SPUtil;
import com.eqdd.library.bean.User;
import com.eqdd.library.bean.rongyun.GroupBean;
import com.eqdd.library.gen.GreenDaoManager;
import com.eqdd.library.gen.GroupBeanDao;
import com.eqdd.library.gen.UserDao;

import java.util.List;

/**
 * Created by lzh on 17-1-5.
 */

public class GreenDaoUtil {
    public static UserDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserDao();

    }

    public static User getUser() {
        List<User> userRecs = getUserDao().queryBuilder()
                .where(UserDao.Properties.Uname.eq(SPUtil.getAccount("username", "")))
                .list();

        return userRecs.size() > 0 ? userRecs.get(0) : null;
    }

    public static void deleteAllUser() {
        getUserDao().deleteAll();
    }


    public static int insert(User user) {
        if (getUser() != null && getUser().getUname().equals(user.getUname())) {
            user.setId(getUser().getId());
            getUserDao().delete(user);
        }
        SPUtil.saveAccount("username", user.getUname());
        return (int) getUserDao().insert(user);
    }


    public static GroupBean getGroupById(String id){
        List<GroupBean> groupBeanList = getGroupDao().queryBuilder()
                .where(GroupBeanDao.Properties.Groupid.eq(id))
                .list();

        return groupBeanList.size()>0?groupBeanList.get(0):null;
    }

    public static GroupBeanDao getGroupDao(){
        return GreenDaoManager.getInstance().getSession().getGroupBeanDao();

    }
    public static int insertGroup(GroupBean bean){
        GroupBean groupById = getGroupById(bean.getGroupid());
        if (groupById!=null) {
            getGroupDao().delete(groupById);
        }
        return (int)getGroupDao().insert(bean);
    }
    public static List<GroupBean> getGroupAll(){
        List<GroupBean> groupBeanList = getGroupDao().loadAll();
        return groupBeanList;
    }
    public static void deleteAllGroup(){
        List<GroupBean> groupBeens = getGroupDao().loadAll();
        if (groupBeens==null||groupBeens.size()==0){
            return;
        }
        getGroupDao().deleteAll();
    }
}
