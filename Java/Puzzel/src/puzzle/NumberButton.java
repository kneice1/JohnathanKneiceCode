package puzzle;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class NumberButton extends JButton {
   private int number;
   private final int row;
   private final int column;

   public NumberButton(int number, int row, int column) {
      setNumber(number);
      this.row = row;
      this.column = column;
   }

   public int getRow() {
      return row;
   }

   public int getColumn() {
      return column;
   }

   public int getNumber() {
      return number;
   }

   public void setNumber(int number) {
      this.number = number;
      if( number == 0 )
         setText("");
      else
         setText("" + number);
      setEnabled(number != 0);
   }

   public boolean isEmpty() {
      return number == 0;
   }
}