package net.devbase.jevrc.viewer;

import net.devbase.jevrc.viewer.models.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.devbase.jevrc.Reader;
import net.devbase.jevrc.Reader.ReaderListener;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTable;

@SuppressWarnings("restriction")
class EvrcViewer extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String EVRC_VIEWER_TITLE = "Čitač saobraćajnih dozvola";

  private static EvrcViewer instance;

  private EvrcDocumentInfoModel documentSectionModel;
  private EvrcVehicleInfoModel vehicleSectionModel;
  private EvrcOwnerInfoModel ownerSectionModel;
  
  private JTable documentInfoTable;
  private JTable vehicleInfoTable;
  private JTable ownerInfoTable;
  
  private JPanel detailsPanel;
  private JPanel buttonsPanel;
  
  private JButton btnPDF;
  private JButton btnExit;
  private JButton btnSrbTxt;

  public EvrcViewer() {
    // TODO: set size and layout
    // TODO: create splash screen
    // TODO: create details screen

	// layout setup
	this.setLayout(new BorderLayout());
	detailsPanel = new JPanel();
	detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
	
	buttonsPanel = new JPanel();
	buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
	
    documentSectionModel = new EvrcDocumentInfoModel();
    documentInfoTable = new JTable();
    documentInfoTable.setModel(documentSectionModel);
    documentInfoTable.setVisible(true);
    documentSectionModel.addTableModelListener(documentInfoTable);
    
    vehicleSectionModel = new EvrcVehicleInfoModel();
    vehicleInfoTable = new JTable();
    vehicleInfoTable.setModel(vehicleSectionModel);
    vehicleInfoTable.setVisible(true);
    vehicleSectionModel.addTableModelListener(vehicleInfoTable);
    
    ownerSectionModel = new EvrcOwnerInfoModel();
    ownerInfoTable = new JTable();
    ownerInfoTable.setModel(ownerSectionModel);
    ownerInfoTable.setVisible(true);
    ownerSectionModel.addTableModelListener(ownerInfoTable);
    
    
    detailsPanel.add(new JLabel("Podaci o dokumentu"));
    detailsPanel.add(documentInfoTable);
    
    detailsPanel.add(new JLabel("Podaci o vozilu"));
    detailsPanel.add(vehicleInfoTable);
    
    detailsPanel.add(new JLabel("Podaci o vlasniku"));
    detailsPanel.add(ownerInfoTable);
    
    btnPDF = new JButton("PDF");
    btnSrbTxt = new JButton("SRB TXT");
    btnExit = new JButton("Exit");
    
    btnPDF.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("PDF button clicked");
		}
    	
    });
    
    btnExit.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		System.out.println("Exit clicked!");
    		System.exit(0);
    	}
    });
    
    buttonsPanel.add(btnPDF);
    buttonsPanel.add(btnSrbTxt);
    buttonsPanel.add(btnExit);    
    
    add(detailsPanel, BorderLayout.CENTER);
    add(buttonsPanel, BorderLayout.SOUTH);
  }

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

private static void createAndShowGUI() {
    // TODO: Enable font anti aliasing
    // TODO: set sr_RS locale as default

    // Create and set up the window
    JFrame frame = new JFrame(EVRC_VIEWER_TITLE);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);

    // TODO: set window icon
    // TODO: set default look and feel
    // TODO: test for Java 1.6 or newer

    // Get the list of terminals
    CardTerminal terminal = null;
    try {
      TerminalFactory factory = TerminalFactory.getDefault();
      terminal = factory.terminals().list().get(0);
    } catch (Exception e) {
      e.printStackTrace();
    }

    EvrcViewer app = EvrcViewer.getInstance();
    app.setFrame(frame);
    frame.getContentPane().add(app, BorderLayout.CENTER);
    frame.pack();

    // Create reader and add model as listener
    Reader reader = new Reader(terminal);
    reader.addCardListener(app.getModel("document"));
    reader.addCardListener(app.getModel("vehicle"));
    reader.addCardListener(app.getModel("owner"));

    frame.setVisible(true);
  }

  public static EvrcViewer getInstance()
  {
    if(instance == null) {
      instance = new EvrcViewer();
    }
    return instance;
  }

  public void setFrame(JFrame frame) {
  }
  
  ReaderListener getModel(String section) {
	  if(section == "document") {
		  return this.documentSectionModel;
	  }
	  else if(section == "vehicle") {
		  return this.vehicleSectionModel;
	  }
	  else if(section == "owner") {
		  return this.ownerSectionModel;
	  }
	  else {
		  return null;
	  }
  }
}
