package com.gluonapplication.views;

import java.io.IOException;

import com.gluonhq.charm.glisten.mvc.View;

import javafx.fxml.FXMLLoader;

public class TernaryView {
	
	//private final String name;
	
	/*public TernaryView (){
		this.name = name;
	}*/
	
	public View getView() {
		View view = null;
		try {
			view = FXMLLoader.load(TernaryView.class.getResource("ternary.fxml"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}
	
	public View getAddView() {
		View view = null;
		try {
			view = FXMLLoader.load(TernaryView.class.getResource("ternaryAddNote.fxml"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}
	
}
