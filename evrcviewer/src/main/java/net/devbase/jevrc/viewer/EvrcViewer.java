package net.devbase.jevrc.viewer;

import net.devbase.jevrc.viewer.models.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import net.devbase.jevrc.EvrcCard;
import net.devbase.jevrc.EvrcInfo;
import net.devbase.jevrc.Reader;
import net.devbase.jevrc.Reader.ReaderListener;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("restriction")
class EvrcViewer
	extends JPanel
	implements Reader.ReaderListener {
  private static final long serialVersionUID = 1L;
  private static final String EVRC_VIEWER_TITLE = "Čitač saobraćajnih dozvola";

  private static EvrcViewer instance;

  private EvrcDocumentInfoModel documentSectionModel;
  private EvrcVehicleInfoModel vehicleSectionModel;
  private EvrcOwnerInfoModel ownerSectionModel;
  private EvrcInfo evrcInfo;
  
  private JTable documentInfoTable;
  private JTable vehicleInfoTable;
  private JTable ownerInfoTable;
  
  private JPanel detailsPanel;
  private JPanel buttonsPanel;
  
  private JButton btnPDF;
  private JButton btnExit;
  
  private JLabel saveLabel;
  private JFrame frame;

  public EvrcViewer() {
    // TODO: set size and layout
    // TODO: create splash screen
    // TODO: create details screen
	this.evrcInfo = null;

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
    
    btnPDF = new JButton("Sačuvaj PDF");
    btnExit = new JButton("Izlaz");
    
    saveLabel = new JLabel("Podaci nisu spremni");
    
    btnPDF.addActionListener(new ActionListener() {
		@Override
		 public void actionPerformed(ActionEvent ev)
	      {
	        System.out.println("PDF button clicked");
	        
	        JFileChooser fc = new JFileChooser();
	        fc.setSelectedFile(new File("saobracajna.pdf"));
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF", new String[] { "pdf" });
	        fc.setFileFilter(filter);
	        int returnVal = fc.showSaveDialog(frame);
	        if (returnVal != 0) {
	          return;
	        }
	        String filename = fc.getSelectedFile().toString();
	        if (!filename.toLowerCase().endsWith(".pdf")) {
	          filename = filename + ".pdf";
	        }
	        PdfReport report = null;
	        if (EvrcViewer.this.evrcInfo != null) {
	          report = new PdfReport(EvrcViewer.this.documentSectionModel, 
	            EvrcViewer.this.vehicleSectionModel, EvrcViewer.this.ownerSectionModel);
	        } else if (EvrcViewer.this.evrcInfo == null) {
	          System.out.println("Card not inserted!");
	        }
	        try
	        {
	          System.out.println("writing to: " + filename);
	          report.write(filename);
	        }
	        catch (Exception e)
	        {
	          e.printStackTrace();
	        }
	      }
    	
    });
    
    btnExit.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		System.out.println("Exit clicked!");
    		System.exit(0);
    	}
    });
    
    buttonsPanel.add(btnPDF);
    buttonsPanel.add(btnExit);
    buttonsPanel.add(saveLabel);
    
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
    // Create and set up the window
    JFrame frame = new JFrame(EVRC_VIEWER_TITLE);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);

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
    
    // Get the screen size
    GraphicsConfiguration gc = frame.getGraphicsConfiguration();
    Rectangle bounds = gc.getBounds();
     
     
    
    
    frame.setSize(600, 700);
    frame.getContentPane().add(app, BorderLayout.CENTER);
    
    // Set the Location and Activate
    Dimension size = frame.getPreferredSize();
    frame.setLocation((int) ((bounds.width / 2) - (size.getWidth() / 2)),
                      (int) ((bounds.height / 2) - (size.getHeight() / 2))); 
    
    frame.setLocation(150, 75);

    // Create reader and add model as listener
    Reader reader = new Reader(terminal);
    reader.addCardListener(app.getModel("document"));
    reader.addCardListener(app.getModel("vehicle"));
    reader.addCardListener(app.getModel("owner"));
    
    reader.addCardListener(app);

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
	  this.frame = frame;
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

@Override
public void inserted(EvrcCard card) {
	try
	{
		this.evrcInfo = card.readEvrcInfo();
		this.saveLabel.setText("Podaci su učitani");
	}
	catch (CardException e)
	{
		e.printStackTrace();
	}
	
}

@Override
public void removed() {
	this.saveLabel.setText("Podaci nisu spremni ili kartica nije ubačena");
	this.evrcInfo = null;
	
}
}
