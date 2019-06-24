package com.neuedu.service;

import com.neuedu.commen.Const;
import com.neuedu.commen.GuavaCache;
import com.neuedu.commen.HigherResponse;
import com.neuedu.commen.StatusUtil;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import com.neuedu.until.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper um;

//登陆
    @Override
    public HigherResponse login(String username, String psw, HttpSession session) {
        if (username == null || username.length()==0)
        {
           return HigherResponse.getResponseFailed("用户名不能为空");
        }
        if (psw == null || psw.length()==0)
        {
           return HigherResponse.getResponseFailed("密码不能为空");
        }
        int i = um.selectUserByUserName(username);
        if (i==0)
        {
           return HigherResponse.getResponseFailed("用户名不存在,请重新输入");
        }
        User user = um.selectUserInfoByUserNameAndPsw(username, psw);
        if (user == null)
        {
            return HigherResponse.getResponseFailed("用户名或密码输入错误");
        }
//获取用户登陆信息 放到Session作用域中
        session.setAttribute(Const.USERSESSION,user);
        return HigherResponse.getResponseSuccess("登陆成功",user);
    }
/*
* 非空判断
* 判断用户名邮箱是都存在
* MD5加密密码
* 注册
* */


//注册
    @Override
    public HigherResponse register(User user) {
        if (!StringUtils.isNotBlank(user.getUsername()))
        {
          return   HigherResponse.getResponseFailed("用户名不能为空");
        }
        if (!StringUtils.isNotBlank(user.getPassword()))
        {
          return   HigherResponse.getResponseFailed("密码不能为空");
        }
        if (!StringUtils.isNotBlank(user.getEmail()))
        {
          return   HigherResponse.getResponseFailed("邮箱不能为空");
        }
        if (!StringUtils.isNotBlank(user.getPhone()))
        {
         return    HigherResponse.getResponseFailed("联系方式错误");
        }
        if (!StringUtils.isNotBlank(user.getQuestion()))
        {
          return   HigherResponse.getResponseFailed("密保问题错误");
        }
        if (!StringUtils.isNotBlank(user.getAnswer()))
        {
          return   HigherResponse.getResponseFailed("密保答案错误");
        }

//判断用户名是否存在
        HigherResponse higherResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (higherResponse.getStatus()==StatusUtil.FAILEDSTATUS)
        {
            return HigherResponse.getResponseFailed("用户名已存在");
        }
//判断邮箱是否存在
        HigherResponse higherResponse1 = checkValid(user.getEmail(), Const.EMAIL);
        if (higherResponse1.getStatus()==StatusUtil.FAILEDSTATUS)
        {
            return HigherResponse.getResponseFailed("邮箱已存在");
        }

//MD5加密密码
        String s = MD5Util.MD5Encode(user.getPassword(), null);
        user.setPassword(s);

//设置用户角色
        user.setRole(Const.COMMONADMIN);

//调用添加的方法
        int insert = um.insert(user);
        if (insert== 0)
        {
            return HigherResponse.getResponseFailed("注册用户失败");
        }
        return HigherResponse.getResponseSuccess("注册成功");
    }
//修改密码
    @Override
    public HigherResponse forgetPsw_Ques(String username) {
        if(!StringUtils.isNotBlank(username))
        {
            return HigherResponse.getResponseFailed("输入的用户名为空");
        }
        HigherResponse higherResponse = this.checkValid(username,Const.USERNAME);
        if (higherResponse.isResponseSuccess())
        {
            return HigherResponse.getResponseFailed("没有此用户");
        }
            String s = um.selectQuesByUserName(username);

            return HigherResponse.getResopnseSuccess(s);
    }

//验证输入的密保问题
    @Override
    public HigherResponse checkAnswer(String username, String password, String answer) {
        if (!StringUtils.isNotBlank(username))
        {
            return HigherResponse.getResponseFailed("用户名不能为空");
        }
        if (!StringUtils.isNotBlank(password))
        {
            return HigherResponse.getResponseFailed("密保问题不能为空");
        }
        if (!StringUtils.isNotBlank(answer))
        {
            return HigherResponse.getResponseFailed("答案不能为空");
        }
            int i = um.queryUsernameAnswerExist(username, password, answer);
            if (i>0)
            {
                String uuid = UUID.randomUUID().toString();
                GuavaCache.putCache(Const.TOKENCACHE,uuid);
                return HigherResponse.getResponseSuccess("检验成功",uuid);
            }
            return HigherResponse.getResponseFailed("检验错误");
    }

//忘记密码中重设密码
    @Override
    public HigherResponse forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (!StringUtils.isNotBlank(username))
        {
            return HigherResponse.getResponseFailed("用户名不能为空");
        }
        if (!StringUtils.isNotBlank(passwordNew))
        {
            return HigherResponse.getResponseFailed("密码不能为空");
        }
        if (!StringUtils.isNotBlank(forgetToken))
        {
            return HigherResponse.getResponseFailed("token不能为空");
        }
        String cache = GuavaCache.getCache(Const.TOKENCACHE);
        if (StringUtils.equals(forgetToken,cache))
        {
            int i = um.updatePswByUsername(username, passwordNew);
            if (i>0)
            {
                return HigherResponse.getResponseFailed("修改密码成功");
            }
        }
        return HigherResponse.getResponseFailed("修改密码失败");
    }

//登陆状态下修改密码
    @Override
    /*
    * 密码不能为空
    * 新密码不能等于旧密码
    * 验证旧密码
    * */
    public HigherResponse updatePasswordInOnlin(HttpSession session,String Oldpassword, String Newpassword) {
        if (!StringUtils.isNotBlank(Oldpassword))
        {
            return HigherResponse.getResponseFailed("旧密码不能为空");
        }
        if (!StringUtils.isNotBlank(Newpassword))
        {
            return HigherResponse.getResponseFailed("新密码不能为空");
        }
        if (StringUtils.equals(Oldpassword,Newpassword))
        {
            return HigherResponse.getResponseFailed("新密码不能与旧密码相同");
        }
        User user = (User)session.getAttribute(Const.USERSESSION);
        if (StringUtils.equals(user.getPassword(),Oldpassword))
        {
            int i = um.updatePswByUsername(user.getUsername(), Newpassword);
            if (i>0)
            {
                return HigherResponse.getResponseSuccess("修改密码成功");
            }
        }
        return HigherResponse.getResponseFailed("修改密码失败");
    }

//登陆状态修改个人信息
    /*
    * 判断是否是登陆状态
    * */
    @Override
    public HigherResponse updateinfomation(HttpSession session, User user) {

        User loginUser = (User)session.getAttribute(Const.USERSESSION);
        if (null == loginUser)
        {
            return HigherResponse.getResponseFailed("请先登录");
        }
        if (StringUtils.equals(loginUser.getPhone(),user.getPhone()))
        {
            return HigherResponse.getResponseFailed("联系方式已存在");
        }
        if (StringUtils.equals(loginUser.getEmail(),user.getEmail()))
        {
            return HigherResponse.getResponseFailed("邮箱已存在");
        }
        int i = um.updateByPrimaryKeySelective(user);
        if (i>0)
        {
            return  HigherResponse.getResponseSuccess("修改成功");
        }
        return HigherResponse.getResponseFailed("修改失败");
    }


    //判断用户名邮箱是否存在
    public HigherResponse checkValid(String val,String type)
    {
        if (!StringUtils.isNotBlank(val))
        {
            return HigherResponse.getResponseFailed("请输入对应的值");
        }
        if (StringUtils.isNotBlank(type))
        {
           if (Const.USERNAME.equals(type))
           {
               int i = um.selectUserByUserName(val);
               if (i>0)
               {
                  return HigherResponse.getResponseFailed("用户名已存在");
               }
           }
           if (Const.EMAIL.equals(type))
           {
               int i = um.selectUserByEmail(val);
               if (i>0)
               {
                   return HigherResponse.getResponseFailed("邮箱以存在");
               }
           }
        }else {
          return   HigherResponse.getResponseFailed("校验类型有误");
        }
        return  HigherResponse.getResponseSuccess("校验通过");
    }
}
