package cn.daoway.sp;

import java.util.List;

public class OrderInfo {
	String contactPerson;
	String phone;
	String address;
	String appointTime;
	
	List<OrderItemInfo> items;
	String note;
	
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAppointTime() {
		return appointTime;
	}
	public void setAppointTime(String appointTime) {
		this.appointTime = appointTime;
	}
	public List<OrderItemInfo> getItems() {
		return items;
	}
	public void setItems(List<OrderItemInfo> items) {
		this.items = items;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
