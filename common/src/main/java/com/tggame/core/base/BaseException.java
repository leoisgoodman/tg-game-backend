/*
 * tg学习代码
 */
package com.tggame.core.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 基本异常类
 *
 * @author lixin 2017-08-03 17:46
 */
public class BaseException extends RuntimeException implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(BaseException.class);

    public BaseException(BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage.toString());
        logger.error("系统发生异常[{}]", exceptionMessage);
    }

    public BaseException(BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage.toString());
        logger.error("系统发生异常[{}],参数为[{}]", exceptionMessage, params);
    }


    public BaseException(String message) {
        super(message);
        logger.error("系统发生异常[{}]", message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        logger.error("系统发生异常[{}],异常为[{}]", message, cause);
    }


    /*异常信息定义*/
    public enum BaseExceptionEnum {

        //-----------------系统异常定义 [9000~9499]--------------------------
        Server_Error("9999", "Server Error"),
        Success("0000", "Success"),
        State_Error("9002", "State msg"),
        Ilegal_Param("9003", "Parameter is ilegal"),
        Empty_Param("9004", "Parameter is empty"),
        Exists("9005", "There is already exists"),
        Result_Not_Exist("9006", "The query result does not exist"),
        Session_Error("9007","会话过期,请重新登录"),

        //-----------------上传文件异常定义[9500~9599]--------------------------
        Build_Exist("9500", "build exist"),

        //-----------------账户异常定义[1000~1050]--------------------------
        Account_Error("1000", "There is Error of Account and Password."),
        Balance_Not_Enough("1001", "账户余额不足"),
        Phone_Error("1001", "Phone format msg."),
        Not_Drawn("1002", "Not drawn now. try again."),
        Bet_Lock("1003", "the bet lock."),


        ;


        public String code;//错误编码
        public String msg;//错误信息

        BaseExceptionEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        //信息提示：不存在
        private static String not_exist(String arg) {
            return arg + " does not exist";
        }

        //信息提示：已经存在
        private static String already_exist(String arg) {
            return arg + " already exists";
        }

        //信息提示：添加失败
        private static String add_failed(String arg) {
            return "add " + arg + " failed";
        }

        @Override
        public String toString() {
            return "{code:" + code + ", msg:\"" + msg + "\"}";
        }

    }
}
