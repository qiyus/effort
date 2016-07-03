package com.vigorx.effort.target;


public class TargetInfo {
	private String title = "测试数据-目标假定";
	private String startDate = "2016/03/25";
	private String endDate = "2016/05/25";
	private boolean status[] = new boolean[28];

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean[] getStatus() {
		return status;
	}

	public void setStatus(boolean[] status) {
		this.status = status;
	}
}
