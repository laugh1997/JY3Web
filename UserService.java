package com.neuedu.service;

import com.neuedu.commen.HigherResponse;
import com.neuedu.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface UserService {

//登陆:根据用户名和密码查询用户信息
    public HigherResponse login(String username, String psw, HttpSession session);

//注册
    public HigherResponse register(User user);

//修改密码:根据用户名查找密保问题
    public HigherResponse forgetPsw_Ques(String username);

//提交密保问题答案
    public HigherResponse checkAnswer(String username,String password,String answer);

//忘记密码的重置密码
    public HigherResponse forgetResetPassword(String username,String passwordNew,String forgetToken);

//登陆状态下修改密码
    public  HigherResponse updatePasswordInOnlin(HttpSession session,String Oldpassword,String Newpassword);

//登陆状态下修改个人信息
    public HigherResponse updateinfomation(HttpSession session,User user);

}

