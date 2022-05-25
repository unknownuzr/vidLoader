package ytdownloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class YTDownloader extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        // DirectoryChooser Init
        DirectoryChooser dirChooser = new DirectoryChooser();
        
        // TextInputs
        TextField urlInput = new TextField();
        urlInput.setPromptText("URL of the Video");
        GridPane.setRowIndex(urlInput, 0);
        TextField outputPath = new TextField();
        outputPath.setPromptText("Storage Path");
        outputPath.setDisable(true);
        GridPane.setRowIndex(outputPath, 2);
        TextField fileName = new TextField();
        fileName.setPromptText("Name of the Videofile");
        GridPane.setRowIndex(fileName, 3);
        
        // MessageLabel
        Label message = new Label();
        message.setVisible(false);
        GridPane.setRowIndex(message, 5);
        // Buttons
        Button browseBtn = new Button();
        browseBtn.setText("Browse");
        browseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File browseFile = dirChooser.showDialog(primaryStage);
                
                if(browseFile != null){
                    String filePath = browseFile.getAbsolutePath();
                    outputPath.setText(filePath);
                    outputPath.setDisable(false);
                }
            }
            });
        GridPane.setRowIndex(browseBtn, 2);
        GridPane.setColumnIndex(browseBtn, 2);
        
        Button dlBtn = new Button();
        dlBtn.setText("Download");
        dlBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String urlIn = urlInput.getText();
                String fileNam = fileName.getText();
                String destination = outputPath.getText();
                System.out.println(urlIn);
                
                if(urlIn.isEmpty() || fileNam.isEmpty() || destination.isEmpty()){
                    message.setText("Please fill every Textfield with Information");
                    message.setVisible(true);
                    System.out.println("Empty Textfield");
                } else {
                    if(message.isVisible() == true) {
                        message.setVisible(false);
                    }
                    //System.out.println("Downloading...");
                    
                    String outputDest = destination + "\\" + fileNam;
                    //System.out.println(urlIn + ", " + outputDest);
                    
                    downloadFile(urlIn, outputDest);
                }
            }
        });
        GridPane.setRowIndex(dlBtn, 4);
        
        GridPane root = new GridPane();
        
        // Add Elements to Scene
        root.getChildren().add(browseBtn);
        root.getChildren().add(dlBtn);
        root.getChildren().add(urlInput);
        root.getChildren().add(outputPath);
        root.getChildren().add(fileName);
        root.getChildren().add(message);
        
        Scene scene = new Scene(root, 500, 350);
        
        primaryStage.setTitle("Videoloader by DK");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public static void downloadFile(String urlInp, String outputPath) {
        // let the Magic happen
        try {
            URL fURL = new URL(getFinalLocation(urlInp));
            HttpURLConnection con = (HttpURLConnection)fURL.openConnection();
            BufferedInputStream in = new BufferedInputStream(con.getInputStream());
            FileOutputStream fo = new FileOutputStream(outputPath);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fo.write(dataBuffer, 0, bytesRead);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getFinalLocation(String address) throws Exception {
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) 
        {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM
                || status == HttpURLConnection.HTTP_SEE_OTHER)
            {
                String newLocation = conn.getHeaderField("Location");
                return getFinalLocation(newLocation);
            }
        }
        return address;
        }
}
