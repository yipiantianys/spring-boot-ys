package org.study.config;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.study.model.SysUser;


/**
 * 密码工具
 * @author ys
 *
 */
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    public static final String ALGORITHM_NAME = "md5"; // 基础散列算法
    public static final int HASH_ITERATIONS = 2; // 自定义散列次数
    //ys 已在用户实体中提供证书盐值的获取方法（loginId + salt + salt）;
    public void encryptPassword(SysUser user) {
        // 随机字符串作为salt因子，实际参与运算的salt我们还引入其它干扰因子
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword = new SimpleHash(ALGORITHM_NAME, user.getPwd(),
                ByteSource.Util.bytes(user.getCredentialsSalt()), HASH_ITERATIONS).toHex();
        user.setPwd(newPassword);
    }
}