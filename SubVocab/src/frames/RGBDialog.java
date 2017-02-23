/**
  * This is an RGB color mixer.
  * 
  * @name RGBDialog
  * @author Csaba Marosi
  */
package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RGBDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	
	/*the currently selected color*/
	
	private Color color = new Color(255, 255, 255);
	
	/* the variable's value turns to false, if we click on cancel*/
	
	private boolean isOk = true;
	
	/*Create the dialog.*/
	public RGBDialog() {
		setBounds(100, 100, 390, 194);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.WEST);
		
		/*labels*/
		
		JLabel lblGreen = new JLabel("Green:");
		lblGreen.setBackground(new Color(0, 128, 0));
		lblGreen.setFont(new Font("Arial", Font.PLAIN, 12));
		JLabel lblRed = new JLabel("Red:");
		lblRed.setFont(new Font("Arial", Font.PLAIN, 12));
		JLabel lblBlue = new JLabel("Blue:");
		lblBlue.setFont(new Font("Arial", Font.PLAIN, 12));
		
		/*active elements*/
		
		JSpinner spinnerRed = new JSpinner();
		spinnerRed.setModel(new SpinnerNumberModel(255, 0, 255, 1));
		spinnerRed.setFont(new Font("Arial", Font.PLAIN, 12));
		JSpinner spinnerBlue = new JSpinner();
		spinnerBlue.setModel(new SpinnerNumberModel(255, 0, 255, 1));
		spinnerBlue.setFont(new Font("Arial", Font.PLAIN, 12));
		JSpinner spinnerGreen = new JSpinner();
		spinnerGreen.setModel(new SpinnerNumberModel(255, 0, 255, 1));
		spinnerGreen.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JPanel panelColor = new JPanel();
		panelColor.setBackground(color);
		
		JSlider sliderRed = new JSlider();
		sliderRed.setBackground(UIManager.getColor("Button.background"));
		JSlider sliderGreen = new JSlider();
		sliderGreen.setBackground(UIManager.getColor("Button.background"));
		JSlider sliderBlue = new JSlider();
		sliderBlue.setBackground(UIManager.getColor("Button.background"));
		
		/*listeners*/
		/*
		* If the spinner changes, it changes the slider's value, refreshes the color's value
		* and set the panel's color.
		*/
		spinnerRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				color = new Color((int) spinnerRed.getValue(),(int) spinnerGreen.getValue(),
						(int) spinnerBlue.getValue());
				sliderRed.setValue((int) spinnerRed.getValue());
				panelColor.setBackground(color);
			}
		});
		
		
		spinnerGreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				color = new Color((int) spinnerRed.getValue(),(int) spinnerGreen.getValue(),
						(int) spinnerBlue.getValue());
				sliderGreen.setValue((int) spinnerGreen.getValue());
				panelColor.setBackground(color);
			}
		});
		
		spinnerBlue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				color = new Color((int) spinnerRed.getValue(),(int) spinnerGreen.getValue(),
						(int) spinnerBlue.getValue());
				sliderBlue.setValue((int) spinnerBlue.getValue());
				panelColor.setBackground(color);
			}
		});
		
		/*
		 * If the slider's value changes, it changes the spinner's value, sets the color,
		 * and sets the panel's color.
		 */
		
		sliderRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				spinnerRed.setValue(sliderRed.getValue());
				color = new Color((int) spinnerRed.getValue(),(int) spinnerGreen.getValue(),
						(int) spinnerBlue.getValue());
				sliderBlue.setValue((int) spinnerBlue.getValue());
			}
		});
		sliderRed.setMaximum(255);
		sliderRed.setFont(new Font("Arial", Font.PLAIN, 12));
		sliderRed.setValue((int) spinnerRed.getValue());
		
		
		sliderGreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				spinnerGreen.setValue(sliderGreen.getValue());
				color = new Color((int) spinnerRed.getValue(),(int) spinnerGreen.getValue(),
						(int) spinnerBlue.getValue());
				sliderBlue.setValue((int) spinnerBlue.getValue());				
			}
		});
		sliderGreen.setMaximum(255);
		sliderGreen.setFont(new Font("Arial", Font.PLAIN, 12));
		sliderGreen.setValue((int) spinnerGreen.getValue());
		
		
		sliderBlue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				spinnerBlue.setValue(sliderBlue.getValue());
				color = new Color((int) spinnerRed.getValue(),(int) spinnerGreen.getValue(),
						(int) spinnerBlue.getValue());
				sliderBlue.setValue((int) spinnerBlue.getValue());
			}
		});
		sliderBlue.setMaximum(255);
		sliderBlue.setFont(new Font("Arial", Font.PLAIN, 12));
		sliderBlue.setValue((int) spinnerBlue.getValue());
		
		/*layout*/
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblRed)
						.addComponent(lblGreen)
						.addComponent(lblBlue))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(spinnerRed, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sliderRed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(spinnerGreen, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sliderGreen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(spinnerBlue, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sliderBlue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(panelColor, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblRed)
							.addComponent(spinnerRed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(sliderRed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(sliderGreen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(spinnerGreen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblGreen)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(sliderBlue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(spinnerBlue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblBlue)))
					.addGap(15))
				.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
					.addComponent(panelColor, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setFont(new Font("Arial", Font.PLAIN, 12));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Arial", Font.PLAIN, 12));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						isOk = false;
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public Color getColor(){
		return color;
	}
	/**
	 * Returns true, if we disposed the window with the ok button.
	 */
	public boolean isOk(){
		return isOk;
	}
}
