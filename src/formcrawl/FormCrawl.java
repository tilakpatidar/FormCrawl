/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formcrawl;

import form.Page;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.apache.commons.lang3.builder.ToStringStyle;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author tilak
 */
public class FormCrawl extends javax.swing.JFrame {

	/**
	 * Creates new form FormCrawl
	 */
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public static ArrayList<WebDriver> drivers = new ArrayList<WebDriver>();

	public FormCrawl() {
		initComponents();

		try {
			MyLogger.setup(LoggerTextArea);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");

		}

		//reset screenshot directory
		LOGGER.log(Level.INFO, "[START] Reset screenshots directory");
		
		try {
			File f = new File("./screenshots");
			FileUtils.deleteDirectory(f);
			//delete screenshot directory
			LOGGER.log(Level.INFO, "[DONE] Deleted screenshots directory");
			File ff = new File("./screenshots");
			ff.mkdir();
			LOGGER.log(Level.INFO, "[DONE] Touched screenshots directory");
			LOGGER.log(Level.INFO, "[DONE] Reset screenshots directory");
		} catch (IOException ex) {
			Logger.getLogger(FormCrawl.class.getName()).log(Level.SEVERE, null, ex);
			LOGGER.log(Level.INFO, "[FAIL] Reset screenshots directory");
		}

		LOGGER.setLevel(Level.FINEST); //set the logging info
		LOGGER.log(Level.INFO, "[DONE] Form UI started");
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form EPager.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                buttonGroup1 = new javax.swing.ButtonGroup();
                jPanel1 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                URLField = new javax.swing.JTextField();
                jButton1 = new javax.swing.JButton();
                jPanel2 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                LoggerTextArea = new javax.swing.JTextArea();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                addWindowListener(new java.awt.event.WindowAdapter() {
                        public void windowClosing(java.awt.event.WindowEvent evt) {
                                closingWindow(evt);
                        }
                });

                jLabel1.setText("Enter the webpage URL");

                URLField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                URLFieldActionPerformed(evt);
                        }
                });

                jButton1.setText("Submit");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(URLField, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton1))
                                        .addComponent(jLabel1))
                                .addContainerGap(365, Short.MAX_VALUE))
                );
                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(URLField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1))
                                .addContainerGap(29, Short.MAX_VALUE))
                );

                LoggerTextArea.setEditable(false);
                LoggerTextArea.setBackground(new java.awt.Color(0, 0, 0));
                LoggerTextArea.setColumns(20);
                LoggerTextArea.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
                LoggerTextArea.setForeground(new java.awt.Color(0, 153, 0));
                LoggerTextArea.setRows(5);
                jScrollPane1.setViewportView(LoggerTextArea);

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addContainerGap())
                );
                jPanel2Layout.setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                .addContainerGap())
                );

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

	
	
	
	
    private void URLFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_URLFieldActionPerformed
	    // TODO add your handling code here:
    }//GEN-LAST:event_URLFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
	    LOGGER.log(Level.INFO, "[START] Page creation started");
	    try {
		    Page mPage = new Page(URLField.getText());
		    LOGGER.log(Level.INFO, "[DONE] Page created");
	    } catch (Exception e) {
		    LOGGER.log(Level.INFO, "[FAIL] Page creation failed {0}", e.getMessage());
		    LOGGER.log(Level.FINEST, "[ERROR] {0}", e.getMessage());
		    e.printStackTrace();
	    }


    }//GEN-LAST:event_jButton1ActionPerformed

        private void closingWindow(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closingWindow
                // TODO add your handling code here:
		for(WebDriver driver : FormCrawl.drivers){
			
			try{
				driver.quit();
			}catch(Exception e){
				
			}
			
		} 
		
		
        }//GEN-LAST:event_closingWindow

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(FormCrawl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> {
			new FormCrawl().setVisible(true);
		});
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JTextArea LoggerTextArea;
        private javax.swing.JTextField URLField;
        private javax.swing.ButtonGroup buttonGroup1;
        private javax.swing.JButton jButton1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JScrollPane jScrollPane1;
        // End of variables declaration//GEN-END:variables
}
