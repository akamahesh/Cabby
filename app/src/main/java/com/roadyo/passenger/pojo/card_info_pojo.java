package com.roadyo.passenger.pojo;

import android.graphics.Bitmap;

public class card_info_pojo {
	
	private Bitmap card_image;
	private String card_numb;
	private String exp_month;
	private String exp_year;
	private String card_id;
	

	public card_info_pojo(Bitmap card_image, String card_numb,
			String exp_month, String exp_year, String id) {
		super();
		this.card_image = card_image;
		this.card_numb = card_numb;
		this.exp_month = exp_month;
		this.exp_year = exp_year;
		this.card_id=id;
	}




	public Bitmap getCard_image() {
		return card_image;
	}

	public void setCard_image(Bitmap card_image) {
		this.card_image = card_image;
	}

	public String getCard_numb() {
		return card_numb;
	}

	public void setCard_numb(String card_numb) {
		this.card_numb = card_numb;
	}


	public String getExp_month() {
		return exp_month;
	}


	public void setExp_month(String exp_month) {
		this.exp_month = exp_month;
	}


	public String getExp_year() {
		return exp_year;
	}


	public void setExp_year(String exp_year) {
		this.exp_year = exp_year;
	}




	public String getCard_id() {
		return card_id;
	}




	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	
	

}
