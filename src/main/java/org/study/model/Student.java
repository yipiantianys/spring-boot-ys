package org.study.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id; 

/**
 * 【JPA自动创建表和字段的实体示例】
 * 说明：
 * 		（1）一定要在实体类和主键上分别加@Entity和@Id注解,属于jpa专用，且@Entity中要指明数据表名，否则根据类名识别
 * 		（2）主键一定要加@GeneratedValue注解，且在springboot中@GeneratedValue中不能加属性内容
 * （学生实体）
 * @author ys
 *
 */
@Entity(name = "student")
public class Student implements Serializable {

	private static final long serialVersionUID = 821160123890313944L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;//id  
	
	@Column(name = "name",length = 100)
	private String name;//姓名
	
	@Column(name = "code",length = 100)
	private String code;//学号
	
	@Column(name = "age")
	private Integer age;//年龄

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	

}
