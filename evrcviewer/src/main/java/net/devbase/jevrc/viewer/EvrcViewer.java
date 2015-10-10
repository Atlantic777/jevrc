package net.devbase.jevrc.viewer;

import java.awt.BorderLayout;

import net.devbase.jevrc.Reader;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.JPanel;
import javax.swing.JFrame;

class EvrcViewer extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String EVRC_VIEWER_TITLE = "Čitač saobraćajnih dozvola";

  private static EvrcViewer instance;

  private JFrame frame;
  private EvrcTableModel tableModel;

  public EvrcViewer() {
    // TODO: set size and layout
    // TODO: create splash screen
    // TODO: create details screen

    tableModel = new EvrcTableModel();
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
    reader.addCardListener(app.getModel());

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

  public EvrcTableModel getModel() {
    return this.tableModel;
  }
}
