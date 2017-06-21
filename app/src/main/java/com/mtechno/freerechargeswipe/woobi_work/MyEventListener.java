package com.mtechno.freerechargeswipe.woobi_work;

import android.content.Context;
import android.widget.Toast;

import com.woobi.WoobiError;
import com.woobi.WoobiEventListener;

public class MyEventListener implements WoobiEventListener {

	private Context context;
	public MyEventListener(Context context)
	{
		this.context = context;
	}
	@Override
	public void onInitialized() {
//		Toast.makeText(context, "Initialized Woobi", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onError(WoobiError error) {
//		Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onShowOffers() {
//		Toast.makeText(context, "onShowOffers", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onCloseOffers() {
//		Toast.makeText(context, "onCloseOffers", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onShowPopup() {
//		Toast.makeText(context, "onShowPopup", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onClosePopup() {
//		Toast.makeText(context, "onClosePopup", Toast.LENGTH_SHORT).show();
		
	}

}
