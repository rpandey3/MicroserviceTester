package com.microtest.utils;

	import java.io.InputStream;
	import java.util.Map;

	public class YamlObject {
		private String name;
	    private int age;
	    private String company;
	    public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		private Map<String, Object> address;
	    private String[] roles;
	    
	    public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }
	    public int getAge() {
	        return age;
	    }
	    public void setAge(int age) {
	        this.age = age;
	    }
	    public Map<String, Object> getAddress() {
	        return address;
	    }
	    public void setAddress(Map<String, Object> address) {
	        this.address = address;
	    }
	    public String[] getRoles() {
	        return roles;
	    }
	    public void setRoles(String[] roles) {
	        this.roles = roles;
	    }
	}


