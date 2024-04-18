package com.lwj.usercenterback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class UserCenterBackApplicationTests {

    @Test
    void testMd5(){
        String str = "123456";
        String s = DigestUtils.md5DigestAsHex(str.getBytes());
        System.out.println(s);
    }
    @Test
    void testRegex(){
        String str = "1lwj";
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean b = m.find();
        System.out.println(b);
    }
    @Test
    void contextLoads() {

    }

}
