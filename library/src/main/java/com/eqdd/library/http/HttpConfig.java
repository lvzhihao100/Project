package com.eqdd.library.http;

/**
 * Created by lvzhihao on 17-3-20.
 */

public class HttpConfig {
    public  final static int GET=0;
    public  final static int POST=1;

//    public  final static String BASE_URL="http://192.168.1.112:8080/";
    public  final static String BASE_URL="http://192.168.1.107:80/";
    public  final static String BASE_URL_NO="http://192.168.1.107:80";

    //face++ 信息
    public  final static String FACE_PLUSPLUS_APIKEY="m4C6Iudq-CoNtB7G_FZAFrMQzxSPrz94";
    public  final static String FACE_PLUSPLUS_APTSECRET="-8w-QyzoBeqhkLTyKzPk4ZA9UIj-Vymp";



    public  final static String REGISTER="User_login.aspx";//普通注册
    // uid
    public  final static String QIYE_REGISTER="Com_login.aspx";//企业注册
    public  final static String OPTION_HANGYE="option_hangye.aspx";//行业
    public  final static String LOGIN="User_enter.aspx";//登录
    public  final static String INVITE_FRIEND="User_AddFriend.aspx";//邀请好友
    public  final static String INVITE_FRIEND_DEAL="User_AddFriend_Deal.aspx";//处理好友请求 uid,friendid,res
    public  final static String INVITE_FRIEND_LIST="User_AddFriend_Request.aspx";//加载好友请求
    public  final static String FRIEND_LIST_INFO="User_GetFriendList.aspx";//获取好友信息列表 uid
    public  final static String GET_TOKEN="User_RcHttpclient.aspx";//获取token uid
//    temail ,aname
    public  final static String USER_EMAIL_CHECK="User_backpass.aspx";//用户邮箱验证码
    // aname,apass
    public  final static String USER_EMAIL_GET_PASSWORD="User_cpass.aspx";//用户邮箱找回密码
    // uid,friendid
    public  final static String FRIEND_DELETE="User_DeleteFriend.aspx";//删除好友
    //uname,name,idnum,sex,nation,birth,pnative,email
    public  final static String USER_AUTHEN="Com_InsertStaffInfo.aspx";//个人实名认证
    //uname,name,idnum,sex,nation,birth,pnative,email
    public  final static String USER_AUTHEN_OVER="User_rname_authen.aspx";//个人实名认证old
    //sym 1 民族
    public  final static String CHOOSE_OPTIONS="Option_content.aspx";//json数据
    public  final static String AREA_OPTIONS="option_areas.aspx";//获取地区
    //uid groupid groupname
    public  final static String CREATE_GROUP="User_CreateGroup.aspx";//建群
    // uid, groupid
    public  final static String QUIT_GROUP="User_QuitGroup.aspx";//建群
    //uid groupid groupname
    public  final static String ADD_GROUP="User_AddGroup.aspx";//拉人入群
    /**
     * 获取群列表
     */
    public  final static String GROUP_LIST_INFO="User_GetGroupsInfo.aspx";
    /**
     * 获取群成员信息    groupid
     */
    public  final static String GROUP_USER_LIST="User_SelectGroupMember.aspx";//
    public  final static String SEARCH_GROUP="User_SelectGroupInfo.aspx";//搜索群 groupname/groupid
    public  final static String UPDATE_GROUP="User_UpdateGroupInfo.aspx";//修改群名字 groupname groupid
    /**
     * 添加部门
     * personid 人事帐号
     * postname 部门名称
     * pparent 父级部门
     * pdescrib 部门简介
     */
    public  final static String CREATE_SECTION="Com_CreateSection.aspx";

    /**
     * 获取公司简单信息
     * cid
     */
    public  final static String GET_COM_SIMPLE_INFO="Com_GetSimpleInfo.aspx";

    /**
     * 获取部门架构
     * personid 人事帐号
     */
    public  final static String GET_SECTION_ARCHITECTURE="Com_SelectSection.aspx";

    /**
     * 修改部门名称
     * pid 部门id
     * pname 部门名称
     */
    public  final static String UPDATE_SECTION_NAME="Com_UpdateSectionName.aspx";

    /**
     * 修改岗位名称
     * careertype 岗位类型
     * careername 岗位名称
     * postname 部门名称
     * postnum 部门id
     * id 岗位id
     */
    public  final static String UPDATE_CAREER="Com_UpdateCareer.aspx";

    /**
     * 企业认证
     * pid 部门id
     * pname 部门名称
     */
    public  final static String COM_AUTHEN="Com_rname_authen.aspx";

    /**
     * 获取用户实名认证后信息
     * uid
     */
    public  final static String GET_AUTHEN_INFO="Com_AddStaff.aspx";

    /**
     * 删除部门
     * pid
     */
    public  final static String DELETE_SECTION="Com_DeleteSection.aspx";
    /**
     * 添加员工至相应部门
     * pid
     */
    public  final static String SECTION_ADD_PERSON="Com_InsertStaff.aspx";

    /**
     * 查询子部门
     * pparent
     */
    public  final static String Select_SECTION="Com_SelectSection_part.aspx";

    /**
     * 添加职位
     * careertype
     * careername
     * postname
     * postnum
     */
    public  final static String ADD_CAREER="Com_CreateCareer.aspx";
    /**
     * 查询某部门下岗位
     * pid
     */
    public  final static String SELECT_CAREER="Com_SelectCareer_Part.aspx";

    /**
     * 查询某部门下职员
     * postnum 部门id
     */
    public  final static String SELECT_EMPLOYMENT="Com_SelectSection_Employment.aspx";
    /**
     * 查询公司岗位
     * cid
     */
    public  final static String SELECT_CAREER_ALL="Com_SelectCareer.aspx";

    /**
     * 入职邀请
     * com(公司名称)
     * sec(邀请部门)
     * people(邀请人)
     * post(邀请人职务)
     * tel(邀请人电话)
     * entel(被邀请人电话)
     * ensec(入职部门)
     * enpost(入职职位)
     */
    public  final static String HR_INVITE="Com_HR_Invite.aspx";

    /**
     * 获取用户所有信息
     * uid
     * pass
     */
    public  final static String GET_USER_ALL_INFO="Com_SelectStaffInfo.aspx";
    /**
     * 更改用户信息
     *
     */
    public  final static String UPDATE_USER_INFO="Com_UpdateStaffInfo.aspx";

    /**
     * 更改用户信息
     *
     */
    public  final static String PERFECT_USER_INFO="Com_InsertStaffInfo.aspx";

    /**
     * 获取用户信息
     *uid
     */
    public  final static String SELECT_STAFF="Com_SelectStaff.aspx";


    /**
     * 获取公司邀请列表
     *uid
     */
    public  final static String GET_COM_INVITE_LIST="Com_SelectInvite.aspx";

    /**
     * 同意加入公司
     *uid
     * cid
     * post
     * status
     */
    public  final static String JOIN_COM="Com_SetPersonnel.aspx";
    /**
     * 同意加入公司
     *uid
     * cid
     * post
     * status
     */
    public  final static String GET_JOB_TYPE="Option_Jobtype.aspx";

    /**
     * 验证是否能够加入公司
     *uid

     */
    public  final static String IS_CAN_REGISTER_COM="User_JudgeExist.aspx";



}
