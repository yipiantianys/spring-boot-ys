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

/**
 * @author ys
 *
 */
@Entity
@Table(name = "sys_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = -8687790154329829056L;
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "role")
    private String role;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "perm_id") })
    private List<SysPermission> permissions;
    
    @ManyToMany
    @JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "user_id") })
    private List<SysUser> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public List<SysUser> getUsers() {
        return users;
    }

    public void setUsers(List<SysUser> users) {
        this.users = users;
    }

}