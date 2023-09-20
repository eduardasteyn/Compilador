package compilador;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Editor {
    
    private Stage stage;

    private ToolBar toolBar = new ToolBar();
    private TextArea code = new TextArea();
    private TextArea output = new TextArea();
    private Label status = new Label();
    private Label lineLabel = new Label();
    private ScrollPane scPane = new ScrollPane();

    private File arquivoAtual = null;

    private String filePath = "";
    private File arquivoGerado = null;
    private int lineCount = 0;
    
    public Editor(Stage stage){
        this.stage = stage;
    }

    // actions events
    private EventHandler<ActionEvent> onNew = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            filePath = "";
            code.setText("");
            output.setText("");
            status.setText(filePath);
        }
    };

    private EventHandler<KeyEvent > onNewShorcut = new EventHandler<KeyEvent >() {
        public void handle(KeyEvent  ke)
        {
            if (new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN).match(ke)) {
                onNew.handle(new ActionEvent());
                ke.consume();
            }
        }
    };

    private EventHandler<ActionEvent> onOpen = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                filePath =file.getAbsolutePath();

                FileReader reader = null;
                try {
                    reader = new FileReader(file);
                    char[] chars = new char[(int) file.length()];
                    reader.read(chars);
                    code.setText(new String(chars));
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                arquivoAtual = file;
                status.setText(filePath);
                output.setText("");
                updateLines();
            }
            
        }
    };

    private EventHandler<KeyEvent > onOpenShorcut = new EventHandler<KeyEvent >() {
        public void handle(KeyEvent  ke)
        {
            if (new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN).match(ke)) {
                onOpen.handle(new ActionEvent());
                ke.consume();
            }
        }
    };

    private EventHandler<ActionEvent> onSave = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            if (filePath == ""){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save");
                fileChooser.getExtensionFilters().addAll();
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    filePath =file.getAbsolutePath();
                }
            } 

            try {
                FileWriter writer = new FileWriter(filePath, false);
                writer.write(code.getText());
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }  
                
            output.setText("");
            status.setText(filePath);
        }
    };

    private EventHandler<KeyEvent > onSaveShorcut = new EventHandler<KeyEvent >() {
        public void handle(KeyEvent  ke)
        {
            if (new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN).match(ke)) {
                onSave.handle(new ActionEvent());
                ke.consume();
            }
        }
    };

    private EventHandler<ActionEvent> onCopy = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
		    final ClipboardContent content = new ClipboardContent();
		    content.putString(code.getSelectedText());
		    clipboard.setContent(content);
        }
    };

    private EventHandler<KeyEvent > onCopyShorcut = new EventHandler<KeyEvent >() {
        public void handle(KeyEvent  ke)
        {
            if (new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN).match(ke)) {
                onCopy.handle(new ActionEvent());
                ke.consume();
            }
        }
    };

    private EventHandler<ActionEvent> onCut = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
		    final ClipboardContent content = new ClipboardContent();
            String text = code.getSelectedText();
		    content.putString(text);
		    clipboard.setContent(content);

            int posIni = code.getCaretPosition(); 
            int posFim = code.getCaretPosition() + text.length();
            
            String slicedText = code.getText();
            slicedText = slicedText.substring(0, posIni) + slicedText.substring(posFim, slicedText.length() - 1);
            code.setText(slicedText);
        }
    };

    private EventHandler<KeyEvent > onCutShorcut = new EventHandler<KeyEvent >() {
        public void handle(KeyEvent  ke)
        {
            if (new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN).match(ke)) {
                onCut.handle(new ActionEvent());
                ke.consume();
            }
        }
    };

    private EventHandler<ActionEvent> onPaste = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            if (clipboard.hasString()) {
              final String text = clipboard.getString();
              int caretPosition = code.getCaretPosition();
              code.insertText(caretPosition,text);
            }
        }
    };

    private EventHandler<KeyEvent > onPasteShorcut = new EventHandler<KeyEvent >() {
        public void handle(KeyEvent  ke)
        {
            if (new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN).match(ke)) {
                onPaste.handle(new ActionEvent());
                ke.consume();
            }
        }
    };

    private EventHandler<ActionEvent> onCompile = new EventHandler<ActionEvent>() {

        public void handle(ActionEvent e)
        {   
            output.setText("");
            Lexico lexico = new Lexico(code.getText());
            Semantico semantico = new Semantico();
            Sintatico sintatico = new Sintatico();

            try {
                sintatico.parse(lexico, semantico);

                output.setText(output.getText() + "Programa compilado com sucesso");
                compilar(semantico.getCodigo());
            } catch (LexicalError e1) {
                output.setText(e1.toString());
            } catch (SyntaticError e1) {
                output.setText(e1.toString());
            } catch (SemanticError e1) {
                output.setText(e1.toString());
            }
        }
    };


    private void compilar(String texto) {
        try {
            if(arquivoAtual != null) {
                arquivoAtual = new File(arquivoAtual.getAbsolutePath());
            } else {
                if (filePath != null){
                    arquivoAtual = new File(filePath);
                } else {
                    arquivoAtual = new File("teste");
                }
                
            }
            salvarCodigoGerado(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private void salvarCodigoGerado(String texto) throws IOException {
        String nomeArquivo = arquivoAtual.getAbsolutePath().replace(".txt", "") + ".il";
        if (arquivoGerado == null) {
            escreverArquivo(nomeArquivo, texto);
            arquivoGerado = new File(nomeArquivo);
        } else {
            escreverArquivo(arquivoGerado.getAbsolutePath(), texto);
            arquivoGerado = new File(arquivoGerado.getAbsolutePath());
        }
    }

    private void escreverArquivo(String nomeArquivo, String textoQuebrado) throws IOException {
        try (
            BufferedReader reader = new BufferedReader(new StringReader(textoQuebrado));
            PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo)); ) {
                reader.lines().forEach(line -> writer.println(line));
        }
    }

    private EventHandler<KeyEvent > onCompileShorcut = new EventHandler<KeyEvent >() {
        public void handle(KeyEvent  ke)
        {
            if (ke.getCode() == KeyCode.F7) {
                onCompile.handle(new ActionEvent());
                ke.consume();
            }
        }
    };

    private EventHandler<ActionEvent> onGroup = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            output.setText(output.getText() + "\n>Equipe:\n  -José Henrique Luckmann\n  -Eduarda Jeniffer Steilein Gislon\n  -Andrei Robson da Silva");
        }
    };

    private EventHandler<KeyEvent > onGroupShorcut = new EventHandler<KeyEvent >() {
        public void handle(KeyEvent  ke)
        {
            if (ke.getCode() == KeyCode.F1) {
                onGroup.handle(new ActionEvent());
                ke.consume();
            }
        }
    };

    private EventHandler<KeyEvent> onType = new EventHandler<KeyEvent>() 
    {
        @Override
        public void handle(KeyEvent keyEvent) 
        {
            updateLines();
        }
    };

    private void updateLines() {
        int l = code.getText().split("\n").length;
        if (lineCount !=l ) {
            lineCount = (l<27? 27:l);
            code.setPrefHeight(lineCount*20.5); 
            
            String textLine="";
            for (int i=1;i<=lineCount;i++)
            {
                textLine += i+"\n";
            }
            
            lineLabel.setText(textLine);
        }

        
    }
    
    public Scene makeScene() {
        //Cria a barra de ferramentas com os botões
        this.addButtomToolBar(this.onNew, "Novo", "Ctrl+N", "icons/new.png");
        this.addButtomToolBar(this.onOpen, "Abrir", "Ctrl+O", "icons/open.png");
        this.addButtomToolBar(this.onSave, "Salvar", "Ctrl+S", "icons/save.png");
        this.addButtomToolBar(this.onCopy, "Copiar", "Ctrl+C", "icons/copy.png");
        this.addButtomToolBar(this.onCut, "Recortar", "Ctrl+X", "icons/cut.png");
        this.addButtomToolBar(this.onPaste, "Colar", "Ctrl+V", "icons/paste.png");
        this.addButtomToolBar(this.onCompile, "Compilar", "F7", "icons/compile.png");
        this.addButtomToolBar(this.onGroup, "Equipe", "F1", "icons/group.png");
        

        this.code.setPrefHeight(Control.USE_COMPUTED_SIZE);
        this.code.setPrefWidth(Control.USE_COMPUTED_SIZE);
        this.code.setFont(Font.font("DejaVu Sans Mono", 13));
        this.code.setOnKeyTyped(onType);
        

        this.lineLabel.setPadding(new Insets(5));
        this.lineLabel.setFont(Font.font("Helvetica Sans Mono", 13));
        
        HBox hBox = new HBox();
        hBox.getChildren().addAll(this.lineLabel, this.code);
        HBox.setHgrow(this.code, Priority.ALWAYS);
        HBox.setHgrow(this.lineLabel, Priority.ALWAYS);

        updateLines();

        HBox hBoxOutput = new HBox();
        hBoxOutput.getChildren().add(this.output);
        HBox.setHgrow(this.output, Priority.ALWAYS);
        this.output.setEditable(false);
        this.output.setFont(Font.font("DejaVu Sans Mono"));
        this.output.setPrefHeight(Control.USE_COMPUTED_SIZE);
        this.output.setPrefWidth(Control.USE_COMPUTED_SIZE);
        
        this.status.setPrefWidth(Control.USE_COMPUTED_SIZE);
        this.status.setPrefHeight(25);
        

        //Cria a cena na tela e incorpora os componentes 
        SplitPane divisao = new SplitPane();
        divisao.setOrientation(Orientation.VERTICAL);
        divisao.getItems().addAll(hBox, hBoxOutput);

        VBox vBox = new VBox(); 
        vBox.getChildren().addAll(this.toolBar, divisao);
        VBox.setVgrow(divisao, Priority.ALWAYS);

        AnchorPane canva = new AnchorPane();
        canva.getChildren().addAll(vBox, this.status);
        AnchorPane.setBottomAnchor(this.status, 0.0);
        AnchorPane.setLeftAnchor(this.status, 0.0);
        AnchorPane.setRightAnchor(this.status, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 25.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        
        Scene scene = new Scene(canva, 960, 600);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, this.onNewShorcut);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this.onSaveShorcut);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this.onOpenShorcut);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this.onCopyShorcut);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this.onCutShorcut);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this.onPasteShorcut);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this.onCompileShorcut);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this.onGroupShorcut);
        scene.getRoot().applyCss();
        
        setScrollAlways(this.code);
        setScrollAlways(this.output);
        
        return scene;
    }

    private void setScrollAlways(Node node){
        ScrollPane scrollPane = (ScrollPane) node.lookup(".scroll-pane");
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
    }

    private void addButtomToolBar(EventHandler<ActionEvent> event, String name, String shortcut, String iconPath){
        Button btn= new Button(name+"\n"+shortcut);
        Image img =  new Image(getClass().getResourceAsStream(iconPath));
        ImageView view = new ImageView(img);
        btn.setGraphic(view);
        btn.setOnAction(event);
        this.toolBar.getItems().add(btn);
    }


    
}
