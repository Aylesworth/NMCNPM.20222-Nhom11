/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mch.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import mch.Application;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class MainFrame extends JFrame {

    private final Container contentPane;

    public MainFrame() {
        contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(Application.getApplicationContext().getBean(NavigationBar.class));
        setTitle("Hệ thống thông tin sức khỏe mẹ và bé");
        setPreferredSize(new Dimension(930, 620));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    public void addNavigationBar() {
        contentPane.add((java.awt.Component) Application.getApplicationContext().getBean(NavigationBar.class), 0);
        repaint();
        pack();
    }

    public <T> void setMainPanel(Class<T> panelClass) {
        if (contentPane.getComponentCount() <= 1) {
            addNavigationBar();
        }
        contentPane.add((java.awt.Component) Application.getApplicationContext().getBean(panelClass), 1);
        repaint();
        pack();
    }

    public <T> void setSinglePanel(Class<T> panelClass) {
        contentPane.removeAll();
        contentPane.add((java.awt.Component) Application.getApplicationContext().getBean(panelClass));
        repaint();
        pack();
    }

}
