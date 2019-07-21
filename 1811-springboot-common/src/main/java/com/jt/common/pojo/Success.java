package com.jt.common.pojo;

import java.util.Date;

public class Success {
	private Long successId;
	private Long seckillId;
	private String userId;
	private Integer state;
	private Date CreateTime;
	public Long getSuccessId() {
		return successId;
	}
	public void setSuccessId(Long successId) {
		this.successId = successId;
	}
	public Long getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
	
}
