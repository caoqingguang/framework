package vko.framework;

import java.util.Date;

public class Person {
	private Long aaa;
	private String name;
	private Date time;
	
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Long getAaa() {
		return aaa;
	}
	
	public void setAaa(Long aaa) {
		this.aaa = aaa;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Person(Long aaa, String name) {
		super();
		this.aaa = aaa;
		this.name = name;
	}
	public Person(){}
	

}
