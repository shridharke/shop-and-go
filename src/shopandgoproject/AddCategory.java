package shopandgoproject;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

class AddCategory extends JFrame implements ActionListener{
	
	int flag;
	DatabaseHandler database;
	JTextField categoryNameField;
	JLabel categoryLabel,noProductsLabel; 
	JCheckBox productsBox[];
	JButton addButton,backButton; 
	String username;
	int productsLength;
	ArrayList productList;
	String categoryName;
	boolean edit;
	JScrollPane productsPane;
	JPanel productsPanel;
	
	AddCategory(String username,String categoryName,boolean edit){
		
		
		this.edit=edit;
		this.categoryName=categoryName;
		database=new DatabaseHandler();
		productList=new ArrayList();
		productList=database.getProducts(username);
		this.username=username;
		
		addButton=new JButton("Add");
		backButton=new JButton("Back");
		categoryNameField=new JTextField();
		categoryLabel=new JLabel("Enter category :");
		noProductsLabel=new JLabel();
		noProductsLabel.setForeground(Color.RED);
		productsPanel=new JPanel();
		productsPanel.setLayout(new BoxLayout(productsPanel,BoxLayout.Y_AXIS));
		
		categoryLabel.setBounds(50,50,150,40);
		categoryNameField.setBounds(200,50,200,40);
		int y=0;
		
		if(edit) {
			ArrayList categoryProductList=new ArrayList();
			categoryProductList=database.getCategoryProducts(username, categoryName);
			for(Object product:categoryProductList) {
				productList.remove(product);
			}
		}
		productsLength=productList.size();
		productsBox=new JCheckBox[productsLength];
		int f=0;
		if(productList.size()==0) {
			f=1;
			//JOptionPane.showMessageDialog(this, "No");
			noProductsLabel.setText("No Products Available!");
			noProductsLabel.setBounds(100,150,200,40);
			categoryNameField.setEditable(false);
			categoryNameField.setEnabled(false);
		}
		else {
			noProductsLabel.setText("");
			noProductsLabel.setBounds(0,0,0,0);
			categoryNameField.setEditable(true);
			categoryNameField.setEnabled(true);
		}
		
		for(int i=0;i<productsLength;i++) {
			
			productsBox[i]=new JCheckBox((String)productList.get(i));
			//productsBox[i].setBounds(0,y,200,40);
				productsPanel.add(productsBox[i]);
	
			y+=30;
		}
		productsPane=new JScrollPane(productsPanel);
		if(f==1) {
			productsPane.setVisible(false);
		}
		else {
			productsPane.setVisible(true);
		}
		productsPane.setBounds(100,100,250,150);
		addButton.setBounds(100,300,200,40);
		addButton.addActionListener(this);
		backButton.setBounds(100,350,200,40);
		backButton.addActionListener(this);
		
		try {
			BufferedImage addIcon = ImageIO.read(new File(new ConstantPath().ICONS_URL+"addIcon.png"));
			Image scaledIcon=addIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			addButton.setIcon(new ImageIcon(scaledIcon));
			BufferedImage backIcon = ImageIO.read(new File(new ConstantPath().ICONS_URL+"backIcon.jpg"));
			scaledIcon=backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			backButton.setIcon(new ImageIcon(scaledIcon));
			backButton.setBorderPainted(false);
			backButton.setContentAreaFilled(false);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e);
		}
		add(noProductsLabel);
		getContentPane().add(productsPane);
		add(categoryNameField);
		add(categoryLabel);
		add(addButton);
		add(backButton);
		if(edit) {
			categoryNameField.setEditable(false);
			categoryNameField.setEnabled(false);
			categoryNameField.setText(categoryName);
		}
		frameInitialize();
	}

	private void frameInitialize() {
		setSize(500, 500);
		setLocation(400, 100);
		setTitle("Add category");
		setLayout(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==addButton) {
			addCategory();
		}
		if(e.getSource()==backButton) {
			setVisible(false);
			new ShopkeeperPage(username);
		}
	}

	private void addCategory() {
		int f=0;
		String categoryName=categoryNameField.getText();
		ArrayList list=new ArrayList();
		list=database.getCategories(username);
		if(categoryName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Enter");
		}
		else if(list.contains(categoryName)&&!edit) {
			JOptionPane.showMessageDialog(this, "Category already exists");
		}
		else {
			for(int i=0;i<productsLength;i++) {
				if(productsBox[i].isSelected()) {
					f=1;
					database.addCategory(username, categoryName,(String) productList.get(i));
				}
			}
		}
		if(f==0) {
			JOptionPane.showMessageDialog(this, "Select something");
		}
		else {
			JOptionPane.showMessageDialog(this, "Added");
			setVisible(false);
			new ShopkeeperPage(username);
		}	
	}
}
