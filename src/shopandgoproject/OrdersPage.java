package shopandgoproject;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;

class OrdersPage extends JFrame implements ActionListener {

	String username;
	JPanel ordersPanel[];
	JPanel ordersView,backgroundPanel;
	JButton deliveryInfoButton[],takeDeliveryButton[],cancelButton[],backButton;
	JScrollPane scrollableView;
	DatabaseHandler database;
	ArrayList customerNameList,customerOrderList,customerUsernameList,orderTimeList;
	JLabel backgroundLabel,noOrderLabel,titleLabel;
	OrdersPage(String username){
		database=new DatabaseHandler();
		customerNameList=new ArrayList();
		customerOrderList=new ArrayList();
		customerNameList=database.getShopkeeperOrderDetails("CUSTOMER_NAME", username);
		customerOrderList=database.getShopkeeperOrderDetails("ORDER_INFO", username);
		customerUsernameList=database.getShopkeeperOrderDetails("CUSTOMER_USERNAME", username);
		orderTimeList=database.getShopkeeperOrderDetails("ORDERED_TIME", username);
		this.username=username;
		ordersPanel=new JPanel[customerNameList.size()];
		deliveryInfoButton=new JButton[customerNameList.size()];
		takeDeliveryButton=new JButton[customerNameList.size()];
		backButton=new JButton();
		cancelButton=new JButton[customerNameList.size()];
		ordersView=new JPanel();
		ordersView.setLayout(new BoxLayout(ordersView,BoxLayout.Y_AXIS));
		backgroundLabel=new JLabel();
		noOrderLabel=new JLabel("",SwingConstants.CENTER);
		titleLabel=new JLabel("Orders",SwingConstants.CENTER);
		backgroundPanel=new JPanel();
		
		for(int i=0;i<customerNameList.size();i++) {
			ordersPanel[i]=new JPanel();
			ordersPanel[i].add(new JLabel(String.valueOf(customerNameList.get(i)).toUpperCase()));
			deliveryInfoButton[i]=new JButton("Info");
			takeDeliveryButton[i]=new JButton("Take Delivery");
			cancelButton[i]=new JButton("Cancel");
			deliveryInfoButton[i].addActionListener(this);
			takeDeliveryButton[i].addActionListener(this);
			cancelButton[i].addActionListener(this);
			ordersPanel[i].add(deliveryInfoButton[i]);
			ordersPanel[i].add(takeDeliveryButton[i]);
			ordersPanel[i].add(cancelButton[i]);
			ordersPanel[i].setBackground(Color.WHITE);
			ordersView.add(ordersPanel[i]);
		}
		ordersView.setBackground(Color.WHITE);
		scrollableView=new JScrollPane(ordersView);
		
		try {
			BufferedImage backIcon = ImageIO.read(new File(new ConstantPath().ICONS_URL+"backIcon.jpg"));
			Image scaledIcon=backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			backButton.setIcon(new ImageIcon(scaledIcon));
			backButton.setBackground(Color.WHITE);
			backButton.setBorderPainted(false);
			backButton.setFocusPainted(false);
			BufferedImage background=ImageIO.read(new File(new ConstantPath().ICONS_URL+"appBackground2.jpg"));
			scaledIcon=background.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
			backgroundLabel.setIcon(new ImageIcon(scaledIcon));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Icon error");
		}
		
		if(customerNameList.size()==0) {
			noOrderLabel.setText("No More orders Available!");
			noOrderLabel.setForeground(Color.RED);
			noOrderLabel.setFont(new Font("Verdana",Font.BOLD,16));
			noOrderLabel.setVisible(true);
			scrollableView.setVisible(false);
		}
		else {
			noOrderLabel.setText("");
			noOrderLabel.setVisible(false);
			scrollableView.setVisible(true);
		}
		
		scrollableView.setBounds(80,100,340,200);
		backButton.setBounds(10,10,40,40);
		noOrderLabel.setBounds(110,150,300,60);
		titleLabel.setBounds(100,0,300,60);
		backgroundPanel.setBounds(0,0,500,60);
		
		backButton.addActionListener(this);
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setFont(new Font("Timesroman",Font.BOLD,24));
		titleLabel.setBackground(Color.WHITE);
		backgroundPanel.setBackground(Color.WHITE);
		backgroundPanel.setBorder(new MatteBorder(1,1,1, 1,Color.BLACK));
		titleLabel.setBorder(new MatteBorder(1,0,1, 0,Color.BLACK));
		backButton.setBorder(new MatteBorder(1,1,1, 1,Color.BLACK));
		titleLabel.setOpaque(true);
		scrollableView.setBackground(Color.WHITE);
		
		setContentPane(backgroundLabel);
		add(titleLabel);
		add(scrollableView);
		add(backButton);
		add(noOrderLabel);
		add(backgroundPanel);
		
		frameInitialize();
	}
	private void frameInitialize() {
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("Orders");
		setLayout(null);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==backButton) {
			setVisible(false);
			new ShopkeeperPage(username);
		}
		else {
			for(int i=0;i<deliveryInfoButton.length;i++) {
				if(e.getSource()==deliveryInfoButton[i]) {
					JOptionPane.showMessageDialog(this,customerOrderList.get(i)+"\nOrdered Time : "+orderTimeList.get(i));
					break;
				}
				if(e.getSource()==takeDeliveryButton[i]) {
					String contactNumber=JOptionPane.showInputDialog(this, "Enter Contact Number");
					
					database.deleteOrder((String)customerUsernameList.get(i),username, (String)orderTimeList.get(i));
					database.updateOrderStatus((String)customerUsernameList.get(i), username, (String)orderTimeList.get(i), "true",contactNumber);
					JOptionPane.showMessageDialog(this,"Delivery Accepted");
					setVisible(false);
					new OrdersPage(username);
					break;
				}
				if(e.getSource()==cancelButton[i]) {
					database.deleteOrder((String)customerUsernameList.get(i),username, (String)orderTimeList.get(i));
					database.updateOrderStatus((String)customerUsernameList.get(i), username, (String)orderTimeList.get(i), "false",null);
					JOptionPane.showMessageDialog(this,"Delivery Cancelled");
					//setVisible(false);
					//new OrdersPage(username);
					repaint();
					break;
				}
			}
		}
	}
}
