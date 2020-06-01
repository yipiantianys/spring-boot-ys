package org.study.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
/**
 * 2020-03-21
 * 1，利用actable实现表的自动生成
 * 		（1）实体上加actable的@Table注解，字段增加actable的@Column注解
 * 2020-03-29
 * 2，整合使用jpa
 * 		（1）一定要在实体类和主键上分别加@Entity和@Id注解,属于jpa专用，且@Entity中要指明数据表名，否则根据类名识别
 * 		（2）主键一定要加@GeneratedValue注解，且在springboot中@GeneratedValue中不能加属性内容
 *  注：jpa自动创建表及字段示例请参考Sutdent实体
 * @author ys
 *
 */
@Entity(name = "sys_user")
@Table(name = "sys_user")
public class SysUser implements Serializable {

	private static final long serialVersionUID = 206671174524425368L;

	@Id
	@GeneratedValue
	@Column(name = "id", type = MySqlTypeConstant.INT, length = 11, isKey = true, isAutoIncrement = true)
	private int id;

	@Column(name = "login_id", type = MySqlTypeConstant.VARCHAR, length = 111,comment="登录名")
	private String loginId;
	
	@Column(name = "pwd", type = MySqlTypeConstant.VARCHAR, length = 111,comment="密码")
	private String pwd;
	
	@Column(name = "name", type = MySqlTypeConstant.VARCHAR, length = 111,comment="用户姓名")
	private String name;
	
	@Column(name = "salt", type = MySqlTypeConstant.VARCHAR, length = 111,comment="加密盐值")
    private String salt;//

	@Column(name = "telephone", type = MySqlTypeConstant.VARCHAR, length = 111,comment="联系电话")
	private String telephone;

	@Column(name = "create_time", type = MySqlTypeConstant.DATETIME,comment="创建时间")
	private Date createTime;
	
	@Column(name = "update_time", type = MySqlTypeConstant.DATETIME,comment="更新时间")
	private Date updateTime;

	@Column(name = "description", type = MySqlTypeConstant.TEXT,comment="描述")
	private String description;

	@Column(name = "age", type = MySqlTypeConstant.BIGINT, length = 5,comment="年龄")
	private Long age;

	@Column(name = "sex", type = MySqlTypeConstant.CHAR, length = 1,comment="性别")
	private String sex;

	@Column(name = "asset", type = MySqlTypeConstant.DOUBLE, length = 5, decimalLength = 2,comment="资产")
	private Double asset;
	
	//jpa自动创建字段示例
	@javax.persistence.Column(name = "mark",length = 1 )
	@Column(name = "mark", type = MySqlTypeConstant.VARCHAR, length = 1, comment="删除标记")
	private String mark;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private List<SysRole> roles;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Double getAsset() {
		return asset;
	}

	public void setAsset(Double asset) {
		this.asset = asset;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	/**
	 * 证书盐值:是用来对用密码进行加密和解密的关键
	 * @return
	 */
	public String getCredentialsSalt() {
        return loginId + salt + salt;
    }
}
