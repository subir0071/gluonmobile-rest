package com.gluonapplication.views;


import java.util.Optional;

import com.gluonapplication.GluonApplication;
import com.gluonapplication.beans.NotesBean;
import com.gluonapplication.beans.NotesBeanWrapper;
import com.gluonhq.charm.glisten.animation.BounceInDownTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListCell;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;

public class TernaryPresenter {

    @FXML
    private View ternaryView;
    
    @FXML
    private CharmListView<NotesBean, Long> nameList;

    public void initialize() {
    	ternaryView.setShowTransitionFactory(BounceInDownTransition::new);
        
    	ternaryView.getLayers().add(new FloatingActionButton(MaterialDesignIcon.ADD.text, 
            e -> openAddView()).getLayer());
        
    	ternaryView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(GluonApplication.MENU_LAYER)));
                appBar.setTitleText("Notes App");
                appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e -> 
                        System.out.println("Favorite")));
            }
        });
    	retrieveNotesList();
    }
    
    private void retrieveNotesList(){
    	RestClient restClient = RestClient.create()
                .host("http://localhost:8090")
                .path("/note/retrieveNoteList")
                .method("GET");
        GluonObservableObject<NotesBeanWrapper> readNoteListResponse = DataProvider.retrieveObject(restClient.createObjectDataReader(NotesBeanWrapper.class));
        
        readNoteListResponse.initializedProperty().addListener((obs, ov, nv) -> {
            if (nv && readNoteListResponse.get() != null) {
            	populateNotesListInUI(readNoteListResponse.get());
            }
        });
    }
    
    private void populateNotesListInUI(NotesBeanWrapper notesBeanWrapper){
    	ObservableList<String> notesListStr = FXCollections.observableArrayList();
    	//notesListStr.add(notesBeanWrapper.getNote()+" "+notesBeanWrapper.getDate());
    	/*for(NotesBean notesBean : notesBeanWrapper.getNotesBeanList()){
    		
    		notesListStr.add(notesBean.getNote());
    		nameList.setit(notesBean);
    	}*/
    	nameList.setItems(FXCollections.observableArrayList(notesBeanWrapper.getNotesBeanList()));
    	//nameList.seth
    	nameList.setCellFactory(p -> new CharmListCell<NotesBean>(){
    	    @Override public void updateItem(NotesBean item, boolean empty) {
    	        super.updateItem(item, empty); 
    	 
    	        if(item != null && !empty){
    	        	HBox buttons = new  HBox();
    	        	Button editBtn = MaterialDesignIcon.EDIT.button();
    	        	editBtn.setId(item.getId());
    	        	editBtn.setOnAction(p -> {
    	        		p.getSource();
    	        		System.out.println("Edit Pressed"+p);
    	        		editNote(((Button)p.getSource()).getId());
    	        		});
    	        	Button delBtn = MaterialDesignIcon.DELETE.button();
    	        	delBtn.setId(item.getId());
    	        	delBtn.setOnAction(e -> {
    	        		deleteNote(((Button)e.getSource()).getId());
    	        	});
    	        	buttons.getChildren().addAll(editBtn, delBtn);
    	        	
    	            ListTile tile = new ListTile();
    	            tile.setId(item.getId());
    	            tile.textProperty().add(item.getNote());
    	            tile.setSecondaryGraphic(buttons);
    	            setText(null);
    	            setGraphic(tile);
    	        } else {
    	            setText(null);
    	            setGraphic(null);
    	        }
    	    }
    	});
    	
    }
    
    public void editNote(String noteId){
    	System.out.println("into Edit Note method");
    	MobileApplication.getInstance().showMessage("In edit");
    }
    
    public void deleteNote(String noteId){
    	System.out.println("into delete note method");
    	Alert alert = new Alert(AlertType.CONFIRMATION, "Confirm Note Deletion!");
    	Optional<ButtonType> ans =alert.showAndWait();
    	ButtonType btnType = ans.orElse(null);
    	String txt = btnType.getText();
    	 if (txt.equalsIgnoreCase("OK"))	{
    		 deleteNotesRestClient(noteId);
    		 MobileApplication.getInstance().showMessage("Note Successfully Deleted");
    		 return;
    	 } else {
    		 MobileApplication.getInstance().showMessage("Note not Deleted");
    	 }
    }
    
    private void deleteNotesRestClient(String noteId){
    	RestClient restClient = RestClient.create()
				.host("http://localhost:8090")
				.path("/note/delete")
				.header("Content-Type", "application/json")
				//.contentType("application/json")
				.header("Accept", "application/json")
				.method("POST");
    	
    	NotesBean chicken = new NotesBean();
		chicken.setId(noteId);
		GluonObservableObject<NotesBean> o = DataProvider.storeObject(chicken,
				restClient.createObjectDataWriter(NotesBean.class));
    }
    
    public void addTextToList() {
    	System.out.println("etxt to list");
    }
    
    public void openAddView(){
    	System.out.println("gondola");
    	MobileApplication.getInstance().switchView(GluonApplication.ADD_VIEW); 
    }
}
