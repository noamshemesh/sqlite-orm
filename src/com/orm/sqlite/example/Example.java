package com.orm.sqlite.example;

import java.util.Arrays;
import java.util.Date;

public class Example {
	private Long id;
	private String str;
	private Boolean bool;
	private Long lng;
	private Integer integer;
	private Date date;
	private Double dbl;
	private Float flt;
	private byte[] byteArray;
	
	public Example(Long id, String str, Boolean bool, Long lng, Integer integer, Date date, Double dbl, Float flt, byte[] byteArray) {
		super();
		this.id = id;
		this.str = str;
		this.bool = bool;
		this.lng = lng;
		this.integer = integer;
		this.date = date;
		this.dbl = dbl;
		this.flt = flt;
		this.byteArray = byteArray;
	}

	public Example() {
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public Boolean getBool() {
		return bool;
	}
	public void setBool(Boolean bool) {
		this.bool = bool;
	}
	public Long getLng() {
		return lng;
	}
	public void setLng(Long lng) {
		this.lng = lng;
	}
	public Integer getInteger() {
		return integer;
	}
	public void setInteger(Integer integer) {
		this.integer = integer;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Double getDbl() {
		return dbl;
	}
	public void setDbl(Double dbl) {
		this.dbl = dbl;
	}
	public Float getFlt() {
		return flt;
	}
	public void setFlt(Float flt) {
		this.flt = flt;
	}
	public byte[] getByteArray() {
		return byteArray;
	}
	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bool == null) ? 0 : bool.hashCode());
		result = prime * result + Arrays.hashCode(byteArray);
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((dbl == null) ? 0 : dbl.hashCode());
		result = prime * result + ((flt == null) ? 0 : flt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((integer == null) ? 0 : integer.hashCode());
		result = prime * result + ((lng == null) ? 0 : lng.hashCode());
		result = prime * result + ((str == null) ? 0 : str.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Example other = (Example) obj;
		if (bool == null) {
			if (other.bool != null)
				return false;
		} else if (!bool.equals(other.bool))
			return false;
		if (!Arrays.equals(byteArray, other.byteArray))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (dbl == null) {
			if (other.dbl != null)
				return false;
		} else if (!dbl.equals(other.dbl))
			return false;
		if (flt == null) {
			if (other.flt != null)
				return false;
		} else if (!flt.equals(other.flt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (integer == null) {
			if (other.integer != null)
				return false;
		} else if (!integer.equals(other.integer))
			return false;
		if (lng == null) {
			if (other.lng != null)
				return false;
		} else if (!lng.equals(other.lng))
			return false;
		if (str == null) {
			if (other.str != null)
				return false;
		} else if (!str.equals(other.str))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Example [id=" + id + ", str=" + str + ", bool=" + bool + ", lng=" + lng + ", integer=" + integer + ", date="
				+ date + ", dbl=" + dbl + ", flt=" + flt + ", byteArray=" + Arrays.toString(byteArray) + "]";
	}
}
