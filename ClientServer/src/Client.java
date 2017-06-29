import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;



/**
 * 
 * @author Okan
 *
 */

public class Client extends Application {

	DataOutputStream toServer = null;
	DataInputStream fromServer = null;

	@Override
	public void start (Stage primaryStage) throws Exception {
		BorderPane fieldPane = new BorderPane();
		fieldPane.setPadding(new Insets(5, 5, 5, 5));
		fieldPane.setLeft(new Label("Enter Roman Value: "));

		TextField field = new TextField();
		field.setAlignment(Pos.BOTTOM_RIGHT);
		fieldPane.setCenter(field);

		BorderPane mainPane = new BorderPane();
		TextArea area = new TextArea();
		area.setEditable(false);
		mainPane.setCenter(new ScrollPane(area));
		mainPane.setTop(fieldPane);

		Scene scene = new Scene(mainPane, 450, 200);
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		primaryStage.show();

		try {
			Socket socket = new Socket("localhost", 8182);

			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			area.appendText(e.toString() + '\n');
		}

		field.setOnAction(e -> {
			try {
				String roman = field.getText();

				toServer.writeUTF(roman);
				toServer.flush();
                                        
				String decimal = fromServer.readUTF();

				area.appendText("Roman value to sent: " + roman + '\n');
				area.appendText("Converting to decimal value: " + decimal + '\n');
			} catch (Exception e2) {
				area.appendText(e2.toString() + '\n');
			}
		});
	}

	public static void main (String[] args) {
		Application.launch(args);
	}
}