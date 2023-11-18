package work.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class EMailUtil {
    public static void send(String text,String targetEmail)
            throws Exception, IOException {
        Properties prop = new Properties();
        prop.setProperty("mail.host","smtp.qq.com"); // 设置qq邮件服务器
        prop.setProperty("mail.transport.protocol","smtp"); // 邮件发送协议
        prop.setProperty("mail.smtp.auth","true"); // 需要验证用户名密码

        //关于QQ邮箱，还要设置ssl加密，加上以下代码既可，其他邮箱不需要！
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        //1.创建定义整个应用程序所需的环境信息的session对象
        //只有qq邮箱有
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("1578586807@qq.com","srzowyycvoakjgdj");
            }
        });
        //开启session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);

        //2.通过session得到transport对象（发送邮件的对象）
        Transport ts = session.getTransport();

        //3.使用邮箱的用户名和授权码连上邮件服务器
        ts.connect("smtp.qq.com", "1578586807@qq.com", "srzowyycvoakjgdj");

        //4.创建邮件
        //需要传递session
        MimeMessage message = new MimeMessage(session);
        //发件人
        message.setFrom(new InternetAddress("1578586807@qq.com"));
        //收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(targetEmail));
        //设置邮件的主体
        message.setSubject("电子商务网站");
        //设置邮件的内容
        message.setContent(text, "text/html;charset=UTF-8");

        //5.发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        //6.关闭连接
        ts.close();
    }
}
