package shopandgoproject;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.email.durgesh.Email;

class PasswordVerification extends JFrame implements ActionListener {

	JLabel backgroundLabel,otpLabel,titleLabel;
	JTextField otpField;
	JButton verifyButton,backButton;
	int flag;
	boolean editable;
	String email,OTP,username;
	
	PasswordVerification(String email,String OTP,int flag,boolean editable,String username){
		this.flag=flag;
		this.editable=editable;
		this.email=email;
		this.OTP=OTP;
		this.username=username;
		otpField=new JTextField();
		backgroundLabel=new JLabel();
		otpLabel=new JLabel("Enter OTP :");
		verifyButton=new JButton("Verify");
		backButton=new JButton("Back");
		titleLabel=new JLabel("OTP has been sent to "+email);
		
		try {
			BufferedImage backIcon = ImageIO.read(new File(new ConstantPath().ICONS_URL+"backIcon.jpg"));
			Image scaledIcon=backIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			backButton.setIcon(new ImageIcon(scaledIcon));
			//backButton.setBackground(Color.WHITE);
			backButton.setBorderPainted(false);
			backButton.setFocusPainted(false);
			backButton.setContentAreaFilled(false);
			BufferedImage background=ImageIO.read(new File(new ConstantPath().ICONS_URL+"appBackground2.jpg"));
			scaledIcon=background.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
			backgroundLabel.setIcon(new ImageIcon(scaledIcon));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Icon error");
		}
		
		otpLabel.setBounds(100,100,100,40);
		otpField.setBounds(200,100,150,40);
		verifyButton.setBounds(120, 170, 150, 40);
		backButton.setBounds(120, 220, 150, 40);
		titleLabel.setBounds(100,50,500,40);
		
		//sampleemailid
		//sampleotphost
		
		verifyButton.addActionListener(this);
		backButton.addActionListener(this);
		
		setContentPane(backgroundLabel);
		add(titleLabel);
		add(otpLabel);
		add(otpField);
		add(verifyButton);
		add(backButton);
		frameInitialize();
		sendEmail();
		
	}
	private void sendEmail() {
		Email email=new Email("samleidforprojecthost@gmail.com", "sampleotphost");
		try {
			email.setFrom("samleidforprojecthost@gmail.com", "Shop And Go");
			email.setSubject("OTP");
			email.setContent("Your OTP is "+OTP,"text/html");
			email.addRecipient(this.email);
			email.send();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Email error");
			new LoginPage();
			setVisible(false);
		}
	}
	private void frameInitialize() {
		setSize(500, 500);
		setLocationRelativeTo(null);
		setTitle("OTP Verification");
		setLayout(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==verifyButton) {
			if(OTP.equals(otpField.getText())) {
				new UserRegister(flag, editable, username);
				setVisible(false);
			}
			else {
				otpField.setText("");
				JOptionPane.showMessageDialog(this, "OTP is Incorrect");
			}
		}
		if(e.getSource()==backButton) {
			setVisible(false);
			new UserLogin(flag);
		}
	}

}
