package application;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.run.ContractRuntime;
import com.conetex.contract.run.ContractRuntime.Informant;
import com.conetex.contract.run.Main;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.util.Pair;
import com.conetex.justus.study.ReadXML;

public class UImain extends Application {

	
	public static class TaskQueue{

		Semaphore ui = new Semaphore();
		
		private Queue<Runnable> uiTodos = new LinkedList<>();
		
		boolean run = true;
		
		public void insertTask(Runnable r){
			synchronized(this){
				this.uiTodos.add(r);
			}
			this.ui.weakup();
		}

		public void doTasks(){
			Runnable r = null;
			while(this.run){
				synchronized(this){
					r = this.uiTodos.poll();
				}				
				while(r != null){
					Platform.runLater(r);
					r = null;
					synchronized(this){
						r = this.uiTodos.poll();
					}
				}
				this.ui.sleep();
			}
		}

	}
	
	static TaskQueue tasks = new TaskQueue();
	
	public static class StringResult {
		String res;
	}
	
	public static class Semaphore {

		public void sleep() {
			synchronized (this) {
				try {
					this.wait();
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void weakup() {
			synchronized (this) {
				this.notify();
			}
		}

	}

	public static class KeyValue {

		private final String	key;
		private final String	value;

		KeyValue(String k, String v) {
			this.key = k;
			this.value = v;
		}

		public String getValue() {
			return this.value;
		}

		public String getKey() {
			return this.key;
		}

	}

	protected static File	inFile;
	protected static Main	main;
    static Semaphore semaphoreRunButWait4Answer = new Semaphore();
	private static void exit() {
		
		UImain.tasks.run = false;
		semaphoreRunButWait4Answer.weakup();
		Platform.exit();
	}

	public Control createFileDropTarget(TreeView<CodeNode> tree, Label messageLabel) {
		Label target = new Label("Drag a file to me.");
		target.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* data is dragged over the target */
				// System.out.println("onDragOver");

				/*
				 * accept it only if it is not dragged from the same node and if
				 * it has a string data
				 */
				if (event.getGestureSource() != target && event.getDragboard().hasFiles()) {
					/*
					 * allow for both copying and moving, whatever user chooses
					 */
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
					System.out.println("onDragOver ok");
				}
				else {
					System.out.println("onDragOver nok");
				}

				event.consume();
			}
		});
		target.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* data dropped */
				System.out.println("onDragDropped");
				/* if there is a string data on dragboard, read it and use it */
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					List<File> listOfFiles = db.getFiles();
					if (listOfFiles.size() > 1) {
						System.err.println("more than one file");
					}
					else if (listOfFiles.size() == 0) {
						System.err.println("no file");
					}
					else {
						File f = listOfFiles.get(0);
						if (f == null) {
							System.err.println("null file");
						}
						else if (f.getName().endsWith(".xml")) {
							target.setText(f.getName());
							try {
								// tree.treeItems.clear();
								UImain.inFile = f;
								UImain.main = ReadXML.in(f);
								CodeNode n = UImain.main.getRootCodeNode();
								TreeItem<CodeNode> i = new TreeItem<CodeNode>(n);
								tree.setRoot(i);

								ObservableList<TreeItem<CodeNode>> childItems = i.getChildren();
								for (CodeNode c : n.getChildNodes()) {
									UImain.load(childItems, c);
								}

								// UImain.load(i.getChildren(), n);
								success = true;
								messageLabel.setText( "loaded " + f.getName() );
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
						else {
							System.err.println("no xml file");
						}
					}
				}
				/*
				 * let the source know whether the string was successfully
				 * transferred and used
				 */
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

			ObservableList<KeyValue> data = FXCollections.observableArrayList();// new
																				// KeyValue("A",
																				// "B")

			Label messageLabel = new Label("");
			
			final Button validButton = new Button("validate");
			validButton.setText("validate");
			validButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					if(UImain.main != null) {
						String msg = ContractRuntime.validateSignatures( UImain.main.getRootStructure() );
						messageLabel.setText( msg );
					}
				}
			});
			
			
			final Button runButton = new Button("run");
			runButton.setText("run");
			runButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (UImain.inFile != null && UImain.main != null) {
						messageLabel.setText( "running ..." );
						Runnable r = new Runnable() {
							@Override
							public void run() {
								try {
									ReadXML.run(UImain.main);
									UImain.tasks.insertTask(new Runnable(){
										@Override
										public void run() {
											messageLabel.setText("run finished!");							
										}
									});
									//ReadXML.out(UImain.main, new File(inFile.getParent(), inFile.getName().replace(".xml", "_out.xml")));
								}
								catch (ParserConfigurationException e) {
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
							}
						};
						Thread s = new Thread(r);
						s.start();
						//Platform.runLater(r);
					}
				}
			});

			final Button saveButton = new Button("sign, save");
			saveButton.setText("sign, save");
			saveButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					try{
						String outFileName = inFile.getName().replace(".xml", "I.xml");
						ReadXML.out(new File(inFile.getParent(), outFileName));
						messageLabel.setText( "saved " + outFileName );
					}
					catch(ParserConfigurationException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(SAXException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(IOException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(AbstractInterpreterException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(AbstractRuntimeException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(AbstractTypException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			TreeItem<CodeNode> rootItem = new TreeItem<CodeNode>();
			rootItem.setExpanded(true);
			TreeView<CodeNode> tree = new TreeView<CodeNode>();

			// tree.setShowRoot(false);
			tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<CodeNode>>() {
				/*
				 * public void changed( ObservableValue<? extends
				 * TreeItem<CodeNode>> observable, TreeItem<String> old_val,
				 * TreeItem<CodeNode> new_val) { TreeItem<CodeNode> selectedItem
				 * = new_val; System.out.println("Selected Text : " +
				 * selectedItem.getValue()); // do what ever you want }
				 */

				@Override
				public void changed(ObservableValue<? extends TreeItem<CodeNode>> observable, TreeItem<CodeNode> old_val, TreeItem<CodeNode> new_val) {
					// TODO Auto-generated method stub
					if (new_val != null) {
						System.out.println("Selected Text : " + new_val.getValue());
						if (old_val == null || new_val.getValue() != old_val.getValue()) {
							data.clear();
							CodeNode n = new_val.getValue();
							if (n != null) {
								try {
									String[] paramNames = n.getParameterNames();
									if (paramNames != null) {
										for (String k : paramNames) {
											data.add(new KeyValue(k, n.getParameter(k)));
										}
									}
									else {
										System.out.println("..." + new_val.getValue());
									}
								}
								catch (UnknownCommandParameter | UnknownCommand e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}

			});

			// Control target = createFileDropTarget(rootItem.getChildren());
			Control target = createFileDropTarget(tree, messageLabel);

			TableView<KeyValue> table = new TableView<>();
			TableColumn keyCol = new TableColumn("key");
			keyCol.setCellValueFactory(new PropertyValueFactory<>("key")); // hier
																			// gehts
																			// weiter
			TableColumn valCol = new TableColumn("value");
			valCol.setCellValueFactory(new PropertyValueFactory<>("value"));
			table.setItems(data);
			table.getColumns().addAll(keyCol, valCol);

			HBox upperArea = new HBox();
			upperArea.getChildren().add(target);
			upperArea.getChildren().add(validButton);
			upperArea.getChildren().add(runButton);
			upperArea.getChildren().add(saveButton);
			
			VBox middleUpperRightArea = new VBox();
			// middleUpperRightArea.getChildren().add(btnAdd);
			// rightArea.getChildren().add(btnDel);
			middleUpperRightArea.getChildren().add(table);

			SplitPane middleUpperArea = new SplitPane();
			middleUpperArea.getItems().add(tree);
			middleUpperArea.getItems().add(middleUpperRightArea);

			SplitPane middleArea = new SplitPane();
			middleArea.setOrientation(Orientation.VERTICAL);
			middleArea.getItems().add(middleUpperArea);
			//middleArea.getItems().add(table);

			VBox mainArea = new VBox();
			mainArea.getChildren().add(upperArea);
			mainArea.getChildren().add(middleArea);
			mainArea.getChildren().add(messageLabel);
			

			StackPane root = new StackPane();
			// root.getChildren().add(btn);
			root.getChildren().add(mainArea);

			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			primaryStage.setOnCloseRequest( x -> exit() );
			
			StringResult res = new StringResult();
			Label questionLabel = new Label("...");
			TextField textField = new TextField();
			Button okButton = new Button("ok");
			okButton.setText("ok");
			okButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("ok!");
					res.res = textField.getText();
					textField.setText("");
					middleUpperRightArea.getChildren().clear();
					middleUpperRightArea.getChildren().add(table);
					semaphoreRunButWait4Answer.weakup();
				}
			});			
			
		
			Runnable r = new Runnable(){
				@Override
				public void run() {
					UImain.tasks.doTasks();
				}
			};
			(new Thread(r)).start();
		
			ContractRuntime.stringAgency.subscribe(new Informant<String>() {

				@Override
				public String getStringAnswer(String question) {

					UImain.tasks.insertTask(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							middleUpperRightArea.getChildren().clear();
							textField.setText("");
							questionLabel.setText(question);
							middleUpperRightArea.getChildren().add(questionLabel);
							middleUpperRightArea.getChildren().add(textField);
							middleUpperRightArea.getChildren().add(okButton);							
						}
						
					});

					semaphoreRunButWait4Answer.sleep();

					//s.enter();
					return res.res;
				}

				@Override
				public String getStringAnswer(String question, Pair<String, String>[] allowedAnswers) {
					// TODO Auto-generated method stub
					return null;
				}

			});

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void load(ObservableList<TreeItem<CodeNode>> items, CodeNode n)
			throws ParserConfigurationException, SAXException, IOException, AbstractInterpreterException, AbstractRuntimeException, AbstractTypException {
		TreeItem<CodeNode> i = new TreeItem<>(n);
		items.add(i);
		ObservableList<TreeItem<CodeNode>> childItems = i.getChildren();
		for (CodeNode c : n.getChildNodes()) {
			load(childItems, c);
		}

	}

}
