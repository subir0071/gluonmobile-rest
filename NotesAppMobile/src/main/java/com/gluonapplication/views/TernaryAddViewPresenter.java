package com.gluonapplication.views;


import com.gluonapplication.beans.NotesBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gluonhq.charm.glisten.animation.BounceInLeftTransition;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TernaryAddViewPresenter {

	@FXML
	View ternaryAddView;
	
	@FXML
	Button addBtn;
	
	@FXML
	TextField nameTxt;
	
	public void initialize() {
		ternaryAddView.setShowTransitionFactory(BounceInLeftTransition::new);
		
	}
	
	/*public void ternaryAddNote(){
		System.out.println("hello there");
	}*/
	
	public void addTextToList(){
		String noteText = nameTxt.getText();
		if(noteText.length() == 0){
			return;
		}
		NotesBean notesbean = new NotesBean();
		notesbean.setNote(noteText);
		ObjectMapper mapper = new ObjectMapper();
		String notesBeanString = "";
		try {
			notesBeanString = mapper.writeValueAsString(notesbean);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("hello there list");
		
		
		RestClient restClient = RestClient.create()
				.host("http://localhost:8090")
				.path("/note/insert")
				.header("Content-Type", "application/json")
				//.contentType("application/json")
				.header("Accept", "application/json")
				.method("POST");
		NotesBean chicken = new NotesBean();
		chicken.setNote(noteText);
		GluonObservableObject<NotesBean> o = DataProvider.storeObject(chicken,
				restClient.createObjectDataWriter(NotesBean.class));
		
		
		
		
		/*OutputStreamOutputConverter<NotesBean> outputConverter = new JsonOutputConverter<>(NotesBean.class);
		InputStreamInputConverter<NotesBean> inputConverter = new JsonInputConverter<>(NotesBean.class);*/
		//GluonObservableObject<NotesBean> sample = DataProvider.retrieveObject(restClient.createObjectDataReader(NotesBean.class));
        
       /* readNoteListResponse.initializedProperty().addListener((obs, ov, nv) -> {
            if (nv && readNoteListResponse.get() != null) {
            	populateNotesListInUI(readNoteListResponse.get());
            }
        });*/
	}
}
