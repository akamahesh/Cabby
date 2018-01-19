package com.roadyo.passenger.pojo;

import java.io.Serializable;
import java.util.ArrayList;

import android.support.v4.os.ParcelableCompat;

public class Support_values implements Serializable
{
	private String tag;
	private String link;
	ArrayList<Support_childs> childs;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public ArrayList<Support_childs> getChilds() {
		return childs;
	}
	public void setChilds(ArrayList<Support_childs> childs) {
		this.childs = childs;
	}


}
