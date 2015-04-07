package com.example.notepad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by piskunovi on 07.04.15.
 */
public class MainPage extends JFrame{

    boolean change = false;

    private JButton btnClick;
    private JPanel rootPanel;
    private JTextArea textArea;
    private JLabel lblTitle;

    public MainPage(){
        super("Notepad");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);

        setJMenuBar(createMenu());

        setVisible(true);
    }

    // создаем меню
    JMenuBar createMenu(){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");


        // кнопка "New"
        JMenuItem newMenu = new JMenuItem("New");
        fileMenu.add(newMenu);

        newMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cleanTextArea(textArea);
            }
        });

        // кнопка "Open"
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea.setText(openFileDialog());
            }
        });

        // кнопка "Save..."
        JMenuItem saveItem = new JMenuItem("Save...");
        fileMenu.add(saveItem);

        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveFileDialog();
            }
        });

        fileMenu.addSeparator();

        // кнопка "Exit"
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuBar.add(fileMenu);

        JMenuItem replaceItem = new JMenuItem("Replace With..");
        editMenu.add(replaceItem);

        replaceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                // Создаем диалог
                final JDialog replaceDialog = new JDialog(MainPage.this, true);
                replaceDialog.setTitle("Replace With...");
                replaceDialog.getContentPane().setLayout(null);
                replaceDialog.setSize(300, 200);

                // Работаем с текстовыми слоями
                JLabel originalLabel = new JLabel("Original");
                JLabel replaceLabel = new JLabel("Replace to");

                originalLabel.setBounds(20,20,120,40);
                replaceLabel.setBounds(20, 70, 120, 40);

                replaceDialog.getContentPane().add(originalLabel);
                replaceDialog.getContentPane().add(replaceLabel);

                // Работаем с текстовыми полями
                final JTextField originalTextField =new JTextField("");
                final JTextField replaceTextField =new JTextField("");

                originalTextField.setBounds(160,20,120,40);
                replaceTextField.setBounds(160,70,120,40);

                replaceDialog.getContentPane().add(originalTextField);
                replaceDialog.getContentPane().add(replaceTextField);

                // Добавляем кнопку подтверждения операции и кнопку отмены
                JButton replaceButton = new JButton("Replace");
                replaceButton.setBounds(20, 125, 120, 40);
                replaceButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String currentText = textArea.getText();
                        textArea.setText(currentText.replaceAll(originalTextField.getText(), replaceTextField.getText()));
                        replaceDialog.setVisible(false);
                    }
                });

                JButton cancelButton = new JButton("Cancel");
                cancelButton.setBounds(160,125,120,40);
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        replaceDialog.setVisible(false);
                    }
                });

                replaceDialog.getContentPane().add(replaceButton);
                replaceDialog.getContentPane().add(cancelButton);

                replaceDialog.setLocationRelativeTo(MainPage.this);
                replaceDialog.setVisible(true);


            }
        });

        menuBar.add(editMenu);

        menuBar.add(helpMenu);

        helpMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                JDialog helpDialog = new JDialog(MainPage.this,"Help", true);

                JPanel helpPanel = new JPanel();
                helpPanel.setLayout(new FlowLayout());

                JTextField originalTextField = new JTextField();
                JTextField replaceTextField = new JTextField();

                helpPanel.add(originalTextField);
                helpPanel.add(replaceTextField);

                helpDialog.add(helpPanel);

                helpDialog.setLocationRelativeTo(MainPage.this);
                helpDialog.setVisible(true);

            }
        });

        return menuBar;

    }

    // открываем текстовый файл
    String openFileDialog() {



        JFileChooser openFile = new JFileChooser();

        int returnVal = openFile.showOpenDialog(MainPage.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String fileText = null; // текст файла
            String lineText;        // текст строки

            StringBuilder builder = new StringBuilder();

            File file = openFile.getSelectedFile();

            try {

                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while ((lineText = bufferedReader.readLine()) != null) {
                    builder.append(lineText + "\n");
                }

                fileText = builder.toString();

                bufferedReader.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return  fileText;
        } else {
            return null;
        }

    }

    void saveFileDialog(){
        JFileChooser saveFile = new JFileChooser();

        int returnVal = saveFile.showSaveDialog(MainPage.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            String fileText = textArea.getText();

            File file = saveFile.getSelectedFile();

            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(fileText);
                bw.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // чистим область TextArea
    void cleanTextArea(JTextArea textArea){
        textArea.setText("");
    }

}
