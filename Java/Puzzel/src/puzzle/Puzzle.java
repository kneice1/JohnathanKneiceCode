package puzzle;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.*;

@SuppressWarnings("serial")
public class Puzzle extends JFrame {

   static {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
      		| UnsupportedLookAndFeelException e) {
      }
   }

   public final int SIZE;
   private NumberButton[][] buttons;
   private int emptyRow;
   private int emptyColumn;

   public static void main(String[] args) {
      int size = 3;

      if(args.length == 1) {
         try {
            size = Integer.parseInt(args[0]);
         }
         catch(NumberFormatException e) {}
      }

      new Puzzle(size);
   }

   public Puzzle(int size) {
      super((size*size - 1) + " Puzzle");
      SIZE = size;
      buttons = new NumberButton[SIZE][SIZE];
      emptyRow = SIZE - 1;
      emptyColumn = SIZE - 1;


      setSize(300,300);

      JPanel game=new JPanel(true);
      game.setLayout(new GridLayout(size, size));
      for (int row=0;row<size;row++){
         for (int column=0;column<size;column++){
            int number=column+row*size+1;
            if(number==size*size){
               number=0;
            }
            NumberButton b=new NumberButton(number,row,column);
            b.setFocusable(false);
            game.add(b);
            buttons[row][column]=b;

            b.addActionListener(e->{
                  NumberButton swap = getSwap(b);
                  if (swap != null) {
                     int bNumber=b.getNumber();
                     int sNumber=swap.getNumber();
                     b.setNumber(sNumber);
                     swap.setNumber(bNumber);
                     emptyRow=b.getRow();
                     emptyColumn=b.getColumn();
                     checkForWin();

                     //TODO: Swap the numbers on the current button and the swap button
                     //TODO: Set emptyRow to the current button's row
                     //TODO: Set emptyColumn to the current button's column
                     //TODO: Call the checkForWin() method

                  }

            });
         }
      }
      pack();
      setSize(300,300);
      setResizable(false);
      getContentPane().add(game);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      scramble();
      setVisible(true);





   	//TODO: Make a JPanel with a SIZE x SIZE GridLayout

   	//TODO: Loop over all rows and columns:
   		//TODO: Determine the number of the current tile, starting at 1 and going up to SIZE*SIZE - 1
   		//		*Important*: The very last button must be numbered 0
   		//TODO: Create a new NumberButton with the given number, row, and column
   		//TODO: Set the button to be non-focusable (by calling its setFocusable() method),
   		// 		purely for aesthetic reasons
   		//TODO: Add the button to the JPanel
   		//TODO: Store the button in the correct row and column within the buttons array
   		//TODO: Add an ActionListener to the button:
   			//TODO: Its actionPerformed() method should call getSwap() on the current button,
   			// 		the return value of getSwap() will be the button to be swapped with
   			//TODO: If the swap button isn't null:
   				//TODO: Swap the numbers on the current button and the swap button
   				//TODO: Set emptyRow to the current button's row
   				//TODO: Set emptyColumn to the current button's column
   				//TODO: Call the checkForWin() method

   	//TODO: Add the JPanel to the Puzzle object
   	//TODO: Set the Puzzle object's default close operation to JFrame.DISPOSE_ON_CLOSE
   	//TODO: Call the pack() method
   	//TODO: Make the Puzzle non-resizable (by calling its setResizable() method)
   	//TODO: Call the scramble() method
   	//		(You can skip the shuffling step at first to make sure your buttons are correctly set up)
   	//TODO: Make the Puzzle visible, by calling its setVisible() method
   }

   private void checkForWin() {
      int count = 0;
      for( int i = 0; i < SIZE; ++i )
         for( int j = 0; j < SIZE; ++j )
            if( isCorrectlyPlaced(buttons[i][j], i, j) )
               count++;

      if( count == SIZE*SIZE - 1 ) {
         for( int i = 0; i < SIZE; ++i )
            for( int j = 0; j < SIZE; ++j )
               buttons[i][j].setEnabled(false);
         JOptionPane.showMessageDialog(this, "You solved the puzzle!", "You win!", JOptionPane.INFORMATION_MESSAGE);
      }

   }

   private boolean isCorrectlyPlaced(NumberButton button, int row, int column) {
      return row * SIZE + column + 1 == button.getNumber();
   }

   public NumberButton[][] getButtons() { return buttons; }

   private NumberButton getSwap( NumberButton button ) {
      int row = button.getRow();
      int column = button.getColumn();
      if( row > 0 && buttons[row - 1][column].isEmpty() )
         return buttons[row - 1][column];
      else if( row < SIZE - 1 && buttons[row + 1][column].isEmpty() )
         return buttons[row + 1][column];
      else if( column > 0 && buttons[row][column - 1].isEmpty() )
         return buttons[row][column - 1];
      else if( column < SIZE - 1 && buttons[row][column + 1].isEmpty() )
         return buttons[row][column + 1];
      else
         return null;
   }

   public int getDimension() {
      return SIZE;
   }

   enum Direction { UP, DOWN, LEFT, RIGHT;

      public int getRowChange() {
         switch(this) {
            case UP:
               return 1;
            case DOWN:
               return -1;
            default:
               return 0;
         }
      }

      public int getColumnChange() {
         switch(this) {
            case LEFT:
               return 1;
            case RIGHT:
               return -1;
            default:
               return 0;
         }
      }
   };

   public void scramble() { scramble(SIZE*SIZE*SIZE*SIZE); }

   private void scramble(int times) {
      Direction[] directions = new Direction[4];
      Random random = new Random();
      for( int i = 0; i < times; i++ ) {
         int availableDirections = 0;
         if( emptyRow > 0 )
            directions[availableDirections++] = Direction.DOWN;
         if( emptyRow < SIZE - 1 )
            directions[availableDirections++] = Direction.UP;
         if( emptyColumn > 0 )
            directions[availableDirections++] = Direction.RIGHT;
         if( emptyColumn < SIZE - 1 )
            directions[availableDirections++] = Direction.LEFT;
         Direction direction = directions[random.nextInt(availableDirections)];
         move(direction, false);
      }
   }

   public void move(Direction direction) {
      move(direction, true);
   }

   private void move(Direction direction, boolean checkForWin) {
   	// Direction is which way a numbered square is moving
   	// which is the OPPOSITE of the way the empty square is moving
      int swapRow = emptyRow + direction.getRowChange();
      int swapColumn = emptyColumn + direction.getColumnChange();

      if( swapRow >= 0 && swapRow < SIZE && swapColumn >= 0 && swapColumn < SIZE ) {
         NumberButton swapButton = buttons[swapRow][swapColumn];
         NumberButton emptyButton = buttons[emptyRow][emptyColumn];
         int swapNumber = swapButton.getNumber();
         int buttonNumber = emptyButton.getNumber();
         swapButton.setNumber(buttonNumber);
         emptyButton.setNumber(swapNumber);
         emptyRow = swapRow;
         emptyColumn = swapColumn;
         if( checkForWin ) // Used when manually clicking
            checkForWin();
      }
   }
}
