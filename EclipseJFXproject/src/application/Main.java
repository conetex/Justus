package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
	        primaryStage.setTitle("Hello World!");
	        
	        SplitPane splitPane1 = new SplitPane();
	        //splitPane1.setPrefSize(200, 200);
	        
	        HBox leftArea = new HBox();
	        HBox rightArea = new HBox();

	        
	        final Button btn = new Button("Left Button");
	        leftArea.getChildren().add(btn);
	        
	        final Button r = new Button("Right Button");
	        rightArea.getChildren().add(r);
	        
	        //splitPane1.getItems().addAll(btn, r);
	        splitPane1.getItems().add(leftArea);
	        splitPane1.getItems().add(rightArea);
	        
	        
	        btn.setText("Say 'Hello World'");
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                System.out.println("Hello World!");
	            }
	        });			
			
	        StackPane root = new StackPane();
			//root.getChildren().add(btn);
			root.getChildren().add(splitPane1);
			
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
}
