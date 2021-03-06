package com.blocktechwh.app.block.Bean;

import com.blocktechwh.app.block.Utils.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
      
	/**
	 *
	 */
	private Integer id;
	/**
	 * 姓名
	 */
	private static String name;
	/**
	 * 手机
	 */
	private String phone;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 公司
	 */
	private String work;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 用户头像
	 */
	private String image;

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone() {
		return this.phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return this.email;
	}

	public void setWork(String work) {
		this.work = work;
	}
	public String getWork() {
		return this.work;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return this.address;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBirthday() {
		return this.birthday;
	}

	public void setSex(String sex) { this.sex = sex.equals("1")?"男":"女"; }
	public String getSex() {
		return this.sex;
	}

	public String getCreateTimeString() {
	return DateUtil.formatDatetime(getCreateTime());
}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateTime() {
		return this.createTime;
	}

	public String getUpdateTimeString() {
	return DateUtil.formatDatetime(getUpdateTime());
}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String getImg(){ return this.image; }
	public void setImg(String img){ this.image = img; }

}