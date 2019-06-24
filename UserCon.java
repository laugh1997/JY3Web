package com.neuedu.contorller;

import com.neuedu.commen.HigherResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/portal/user")
public class UserCon {

    @Autowired
    private UserService userService;
//登陆
    @ResponseBody
    @RequestMapping("/login.do")
    public HigherResponse login(String userName, String psw, HttpSession session)
    {
        return userService.login(userName, psw,session);
    }

//注册
    @ResponseBody
    @RequestMapping("/register.do")
    public HigherResponse register(User user)
    {
        return  userService.register(user);
    }

//忘记密码
    @ResponseBody
    @RequestMapping("/forget_get_question.do")
    public HigherResponse queryQues(String userName)
    {
        return userService.forgetPsw_Ques(userName);
    }

//验证密保问题修改密码
    @ResponseBody
    @RequestMapping("/forget_check_answer.do")
    public HigherResponse forget_check_answer(String username,String question,String answer)
    {
        return userService.checkAnswer(username,question,answer);
    }

//忘记密码中的重设密码   -- 未登录状态
    @ResponseBody
    @RequestMapping("/forget_reset_password.do")
    public HigherResponse forget_reset_password(String username,String passwordNew,String forgetToken)
    {
        return userService.forgetResetPassword(username,passwordNew,forgetToken);
    }

//登陆状态下修改密码
    @ResponseBody
    @RequestMapping("/reset_password.do")
    public HigherResponse reset_password(HttpSession session,String Oldpassword,String Newpassword)
    {
        return userService.updatePasswordInOnlin(session,Oldpassword,Newpassword);
    }

//登陆状态下更新个人信息
    @ResponseBody
    @RequestMapping("/update_information.do")
    public HigherResponse update_information(HttpSession session,User user)
    {
        return userService.updateinfomation(session,user);
    }

}
