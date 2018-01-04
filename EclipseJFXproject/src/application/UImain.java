package application;
	
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.run.Main;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.justus.study.ReadXML;

public class UImain extends Application {
	
	//static ObservableList<TreeItem<CodeNode>> treeItems;
	
	
	public Control createFileDropTarget(ObservableList<TreeItem<CodeNode>> treeItems) {
		Label target = new Label("Drag a file to me.");
        target.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                //System.out.println("onDragOver");
                
                /* accept it only if it is  not dragged from the same node 
                 * and if it has a string data */
                if (event.getGestureSource() != target &&
                        event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    System.out.println("onDragOver ok");
                }
                else{
                	System.out.println("onDragOver nok");
                }
                
                event.consume();
            }
        });
        target.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                	List<File> listOfFiles = db.getFiles();
                	if(listOfFiles.size() > 1){
                		System.err.println("more than one file");		
                	}
                	else if(listOfFiles.size() == 0){
	                		System.err.println("no file");
                	}
                	else{
                		File f = listOfFiles.get(0);
                		if(f == null){
	                		System.err.println("null file");	                			
                		}
                		else if(f.getName().endsWith(".xml")){
        	                target.setText(f.getName());
        	                try {
								treeItems.clear();
								Main main = ReadXML.in( f );
								CodeNode n = main.getRootCodeNode();
								UImain.load(treeItems, n);
	        	                success = true;
							}
							catch (ParserConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							catch (AbstractInterpreterException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							catch (AbstractRuntimeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							catch (AbstractTypException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
            			}
            			else{
            				System.err.println("no xml file");
            			}
                	}
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);
                
                event.consume();
            }
        });
        return target;
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
	        primaryStage.setTitle("Justus Contract View");
	        
	
	        
	        final Button btnAdd = new Button("add Button");
	        btnAdd.setText("add");
	        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                System.out.println("add!");
	                
                    //TreeItem<String> newEmployee = new TreeItem<String>("New Item");
                    //UImain.treeItems.add(newEmployee);	                
	                
	            }
	        });	
	        
	        final Button btnDel = new Button("del Button");
	        btnDel.setText("del");
	        btnDel.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                System.out.println("del!");
	       //         if(! rootItem.getChildren().isEmpty()){
	                	 //UImain.treeItems.remove(0);
	       //         }
	            }
	        });	      
	        
	        TreeItem<CodeNode> rootItem = new TreeItem<CodeNode> ();
	        rootItem.setExpanded(true);
	        TreeView<CodeNode> tree = new TreeView<CodeNode> (rootItem);
	        
	        Control target = createFileDropTarget(rootItem.getChildren());

	        TableView<CodeNode> table = new TableView<>();
	        TableColumn keyCol = new TableColumn("key");
	        keyCol.setCellValueFactory(      new PropertyValueFactory<>("lastName")   ); hier gehts weiter 
	        TableColumn valCol = new TableColumn("value");
	        
	        HBox rightArea = new HBox();
	        rightArea.getChildren().add(btnAdd);
	        rightArea.getChildren().add(btnDel);
	        
	        SplitPane splitPane1 = new SplitPane();
	        splitPane1.getItems().add(tree);
	        splitPane1.getItems().add(rightArea);
	        
	        VBox mainArea = new VBox();
	        mainArea.getChildren().add(target);
	        mainArea.getChildren().add(splitPane1);
	        mainArea.getChildren().add(table);
			
	        StackPane root = new StackPane();
			//root.getChildren().add(btn);
			root.getChildren().add(mainArea);
			
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
		
	public static void load(ObservableList<TreeItem<CodeNode>> items, CodeNode n) throws ParserConfigurationException, SAXException, IOException, AbstractInterpreterException, AbstractRuntimeException, AbstractTypException{
		TreeItem<CodeNode> i = new TreeItem<>(n);
		items.add(i);
		ObservableList<TreeItem<CodeNode>> childItems = i.getChildren();
		for(CodeNode c : n.getChildNodes()){
			load(childItems, c);
		}
		
	}
	
	
}
