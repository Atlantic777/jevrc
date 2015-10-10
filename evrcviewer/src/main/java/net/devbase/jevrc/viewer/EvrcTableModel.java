package net.devbase.jevrc.viewer;

import net.devbase.jevrc.Reader.ReaderListener;
import net.devbase.jevrc.EvrcCard;

class EvrcTableModel implements ReaderListener{

  public void inserted(final EvrcCard card) {
    System.out.println("Card inserted");
  }

  public void removed() {
    System.out.println("Card removed");
  }

}
