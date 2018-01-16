package com.gluonapplication.views;


import com.gluonapplication.GluonApplication;
import com.gluonapplication.beans.NotesBean;
import com.gluonapplication.beans.NotesBeanWrapper;
import com.gluonhq.charm.glisten.animation.BounceInDownTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
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

public class TernaryPresenter {

    @FXML
    private View ternaryView;
    
    @FXML
    private CharmListView<String, Long> nameList;

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
    	for(NotesBean notesBean : notesBeanWrapper.getNotesBeanList()){
    		
    		notesListStr.add(notesBean.getNote());
    	}
    	nameList.setItems(notesListStr);
    	
    	nameList.setCellFactory(p -> new CharmListCell<String>(){
    	    @Override public void updateItem(String item, boolean empty) {
    	        super.updateItem(item, empty); 
    	 
    	        if(item != null && !empty){
    	            ListTile tile = new ListTile();
    	            tile.setPrimaryGraphic(MaterialDesignIcon.EDIT.graphic());
    	           // tile.textProperty().addAll(item);
    	            setText(item);
    	            setGraphic(tile);
    	        } else {
    	            setText(null);
    	            setGraphic(null);
    	        }
    	    }
    	});
    	
    }
    
    public void addTextToList() {
    	System.out.println("etxt to list");
    }
    
    public void openAddView(){
    	System.out.println("gondola");
    	MobileApplication.getInstance().switchView(GluonApplication.ADD_VIEW); 
    }
}
