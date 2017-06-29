import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;



/**
 * 
 * @author okan
 *
 */

public class Server extends Application {

	private int clientNo = 0;
	private TextArea ta = new TextArea();

	@Override
	public void start (Stage primaryStage) throws Exception {
		ta.setEditable(false);

		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Server");
		primaryStage.show();

		new Thread( () -> {
			try {
				ServerSocket serverSocket = new ServerSocket(8182);

				Platform.runLater( () -> {
					ta.appendText("Server started time " + new Date() + '\n');
				});

				while (true) {
					Socket socket = serverSocket.accept();
					clientNo++;

					Platform.runLater( () -> {
						ta.appendText("Starting client " + clientNo + " at " + new Date() + '\n');
						ta.appendText("Client " + clientNo + " IP Address is " + socket.getInetAddress().getHostAddress() + '\n');
					});

					new Thread(new ThreadClient(socket)).start();
				}
			} catch (Exception e) {
				ta.appendText(e.toString() + '\n');
			}
		}).start();
	}

	class ThreadClient implements Runnable {

		private Socket socket;

		public ThreadClient(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run () {
			try {
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

				while (true) {
					String roman = inputFromClient.readUTF();
                                        int decimal = romanToDecimal(roman);
                                        String decimal1 = Integer.toString(decimal);
                                        outputToClient.writeUTF(decimal1);
                                        
					Platform.runLater( () -> {
						ta.appendText("Text received from client: " + roman + '\n');
						ta.appendText("Converting to this " + decimal1  + '\n');
					});
				}
			} catch (Exception e) {
				ta.appendText(e.toString() + '\n');
			}
		}
	}


    public static int romanToDecimal(String romanNumber) {
        int decimal = 0;
        int lastNumber = 0;
        String romanNumeral = romanNumber.toUpperCase();
        
        
        for (int x = romanNumeral.length() - 1; x >= 0 ; x--) {
            char convertToDecimal = romanNumeral.charAt(x);

            switch (convertToDecimal) {
                case 'M':
                    decimal = processDecimal(1000, lastNumber, decimal);
                    lastNumber = 1000;
                    break;

                case 'D':
                    decimal = processDecimal(500, lastNumber, decimal);
                    lastNumber = 500;
                    break;

                case 'C':
                    decimal = processDecimal(100, lastNumber, decimal);
                    lastNumber = 100;
                    break;

                case 'L':
                    decimal = processDecimal(50, lastNumber, decimal);
                    lastNumber = 50;
                    break;

                case 'X':
                    decimal = processDecimal(10, lastNumber, decimal);
                    lastNumber = 10;
                    break;

                case 'V':
                    decimal = processDecimal(5, lastNumber, decimal);
                    lastNumber = 5;
                    break;

                case 'I':
                    decimal = processDecimal(1, lastNumber, decimal);
                    lastNumber = 1;
                    break;
            }
        }
        return decimal;
    }

    public static int processDecimal(int decimal, int lastNumber, int lastDecimal) {
        if (lastNumber > decimal) {
            return lastDecimal - decimal;
        } else {
            return lastDecimal + decimal;
        }
    }

	public static void main (String[] args) {
		Application.launch(args);
	}
}