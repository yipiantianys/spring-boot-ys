package org.study.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sys_permission")
public class SysPermission implements Serializable {
    private static final long serialVersionUID = 353629772108330570L;
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name")
    private String name;//权限功能名称
    
    @Column(name = "url")
    private String url;//权限功能url
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_role_permission", joinColumns = { @JoinColumn(name = "perm_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private List<SysRole> roles;

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

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
    
}