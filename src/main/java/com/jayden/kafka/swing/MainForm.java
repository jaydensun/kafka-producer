package com.jayden.kafka.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private JTextField brokerTextField;
    private JTextField topicTextField;
    private JTextArea msgTextArea;
    private JButton sendButton;

    public MainForm() {
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resultLabel.setText("");
                SendResult sendResult = KafkaSender.send(brokerTextField.getText(), topicTextField.getText(), msgTextArea.getText(),
                        oneMsgRadioButton.isSelected(), trimCheckBox.isSelected());
                if (sendResult.isSuccess()) {
                    resultLabel.setText("成功发送" + sendResult.getCount() + "条消息");
                } else {
                    resultLabel.setText("失败：" + sendResult.getErrorMsg());
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("KAFKA消息发送器");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 800;
        int height = 400;
        frame.setMinimumSize(new Dimension(width, height));
        Dimension size = frame.getToolkit().getScreenSize();
        frame.setLocation((size.width - width) / 2, (size.height - height) / 2);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JLabel resultLabel;
    private JRadioButton oneMsgRadioButton;
    private JRadioButton multiMsgRadioButton;
    private JCheckBox trimCheckBox;
}
