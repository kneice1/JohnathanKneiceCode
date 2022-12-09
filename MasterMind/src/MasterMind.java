//package mastermind;

import com.sun.java.swing.action.OpenAction;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

// COMP 2000 Lab 12
// Spring 2021
//
// Johnathan Kneice
//

public class MasterMind extends JFrame {

   private MasterMind masterMindFrame;  // handy for use by JOptionPane and listeners
   private MasterMind masterMindCustomFrame;
   private MMGame game;




   private final int MAX_GUESSES = 12;
   private final int NUM_PEGS = 4;
   private final int NUM_COLORS = 6;  // maximum 10
   private final int DEFAULT_COLORS_INDEX = 0;
   private Color[] colors = { Color.white, Color.black, Color.blue, Color.red,
                      Color.green, Color.yellow, Color.cyan, Color.orange, 
      					 Color.magenta, Color.pink };

   private Dimension historyScrollPaneSize, buttonSize, movesSize,
                     moveNumSize, historyPanelSize, responsePanelSize;

   // History of past moves and responses
   private JButton[][] history;
   private JLabel[][] response;

   public MasterMind() {
     // constructor is complete if you follow the solution strategy I described
      masterMindFrame = this;
      game = new MMGame(NUM_PEGS, NUM_COLORS);
      this.setDefaultCloseOperation( EXIT_ON_CLOSE );
      this.setSize(400,400);
      this.setTitle("MasterMind");
      this.setResizable(true);
      this.setLocation(250,150);
      this.menuSetup();
      this.sizesSetup();
      JPanel inputPanel = this.inputSetup();
      JPanel historyPanel = this.historySetup();
      JScrollPane historyScrollPane = new JScrollPane(historyPanel);
      historyScrollPane.setPreferredSize(historyScrollPaneSize);
      Container contentPane = getContentPane();
      contentPane.setLayout( new FlowLayout() );
      contentPane.add(historyScrollPane);
      contentPane.add(inputPanel);
   }

// Set up the menu bar, menus and menu items
   private void menuSetup() {
      JMenuBar menuBar = new JMenuBar();
      JMenu gameMenu = new JMenu("Game");
      JMenuItem exitApp = new JMenuItem("Exit");
      exitApp.addActionListener( new ExitListener() );
      JMenuItem openApp = new JMenuItem("Open...");
      openApp.addActionListener(new MenuItemStubListener());
      JMenuItem newApp = new JMenuItem("New");
      newApp.addActionListener(new NewListener() );
      JMenuItem closeApp = new JMenuItem("Close");
      closeApp.addActionListener(new CloseListener());
      JMenuItem saveApp = new JMenuItem("Save");
      saveApp.addActionListener(new MenuItemStubListener());
      JMenuItem saveAsApp = new JMenuItem("Save As...");
      saveAsApp.addActionListener(new MenuItemStubListener());
      JMenuItem customGame= new JMenuItem("Custom Game");
      customGame.addActionListener(new MenuItemStubListener());


      gameMenu.add(newApp);
      gameMenu.add(openApp);
      gameMenu.add(closeApp);
      gameMenu.addSeparator();
      gameMenu.add(saveApp);
      gameMenu.add(saveAsApp);
      gameMenu.addSeparator();
      gameMenu.add(customGame);
      gameMenu.add(exitApp);
      menuBar.add(gameMenu);

      JMenu helpMenu = new JMenu("Help");
      JMenuItem solutionApp = new JMenuItem("View Solution");
      solutionApp.addActionListener(new ShowSolutionListener());
      JMenuItem rulesApp = new JMenuItem("MasterMind Rules");
      rulesApp.addActionListener(new RulesListener());
      JMenuItem aboutApp = new JMenuItem("About...");
      aboutApp.addActionListener(new AboutListener());

      helpMenu.add(solutionApp);
      helpMenu.add(rulesApp);
      helpMenu.add(aboutApp);
      menuBar.add(helpMenu);

      this.setJMenuBar(menuBar);
   }
   private JButton makeGuess= new JButton("Make Guess");
   private JButton[] inputButtons=new JButton[NUM_PEGS];




// Set up JPanel where user does input
   private JPanel inputSetup() {
      JPanel inputButtonPanel = new JPanel();
      for (int i=0;i<NUM_PEGS;++i){
         inputButtons[i]=new JButton();
         inputButtons[i].setBackground(colors[DEFAULT_COLORS_INDEX]);
         inputButtons[i].setPreferredSize(buttonSize);
         inputButtons[i].addActionListener(new InputButtonListener());
         inputButtonPanel.add(inputButtons[i],BorderLayout.NORTH);
      }
      makeGuess.addActionListener(new MakeGuessListener());
      inputButtonPanel.add(makeGuess, BorderLayout.SOUTH);
      return inputButtonPanel;
   }


// Create the various Dimension objects for preferred sizes.
   private void sizesSetup() {
      buttonSize = new Dimension(20,20);
      movesSize = new Dimension(20+65*NUM_PEGS, 36 * MAX_GUESSES);
      moveNumSize = new Dimension(20, 25);
      historyPanelSize = new Dimension(40*NUM_PEGS, 30);
      responsePanelSize = new Dimension(25*NUM_PEGS, 25);      
      historyScrollPaneSize = new Dimension(40+65*NUM_PEGS, 250);
   }


 // set up JPanel with guess and response history
   private JPanel historySetup() {
   
      history = new JButton[MAX_GUESSES][NUM_PEGS];
      response = new JLabel[MAX_GUESSES][NUM_PEGS];
      JPanel[] historyPanel = new JPanel[MAX_GUESSES];
      JPanel[] responsePanel = new JPanel[MAX_GUESSES];
      JPanel moves = new JPanel();
   
      moves.setPreferredSize(movesSize);
   
      for (int i=0; i<MAX_GUESSES; i++) {
         historyPanel[i] = new JPanel();
         historyPanel[i].setPreferredSize(historyPanelSize);
         responsePanel[i] = new JPanel();
         responsePanel[i].setPreferredSize(responsePanelSize);
         JLabel moveNum = new JLabel(new Integer(i+1).toString());
         moveNum.setPreferredSize(moveNumSize);
         historyPanel[i].add(moveNum);
         for (int j=0; j<NUM_PEGS; j++) {
            history[i][j] = new JButton();
            history[i][j].setBackground(Color.gray);
            history[i][j].setBorderPainted(false);
            history[i][j].setFocusPainted(false);
            history[i][j].setOpaque(true);
            history[i][j].setEnabled(false);
            history[i][j].setPreferredSize(buttonSize);
            historyPanel[i].add(history[i][j]);
            response[i][j] = new JLabel(" ");
            response[i][j].setFont(new Font("Monospaced",Font.BOLD,12));
            responsePanel[i].add(response[i][j]);
         }     
         moves.add(historyPanel[i]);
         moves.add(responsePanel[i]);
      }
      return moves;
   }


  // Main, to make this class executable.
   public static void main(String[] args) {
      MasterMind game = new MasterMind();
      game.setVisible(true);
   }

  // Action listener for the Exit button.
   private class ExitListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         System.exit(0);
      }
   }
   private class MenuItemStubListener implements ActionListener {
      public void actionPerformed(ActionEvent e){
         String name= e.getActionCommand();
         System.out.println("Menu item "+name+" is not implemented");
      }
   }
   /*
   boolean isCustom=false;
   int numberColor=0;
   int numberPegs=0;
   private class CustomGameListener implements ActionListener {
      public void actionPerformed(ActionEvent e){
         Scanner MyIn=new Scanner(System.in);
         System.out.println("How many Colors: ");
         numberColor=MyIn.nextInt();
         System.out.println("How many pegs/input buttons: ");
         numberPegs=MyIn.nextInt();
      }
   }
    */

   private class AboutListener implements ActionListener {
      public void actionPerformed(ActionEvent e){
        JOptionPane.showMessageDialog(null,"Created by Johnathan kneice,CopyRight 2021.\n" +
                "Mastermind is a registered trademark of Pressman Toy Corporation.");
      }
   }
   private class RulesListener implements ActionListener {
      public void actionPerformed(ActionEvent e){
         JOptionPane.showMessageDialog(null,
                 "The goal of Mastermind is to guess the computer-generated patter.\n" +
                 "1) You have a certain number of turns to do this.\n" +
                 "2) If you have guessed one of the colors, but it is not in the right spot,\n" +
                         " you will get a white O on the right.\n" +
                 "3) If you guessed one of the colors and it is in the right spot you will get\n" +
                         " a black O on the right.");
      }
   }
   private class ShowSolutionListener implements ActionListener {
      public void actionPerformed(ActionEvent e){
         gameIsOver();
         makeGuess.setEnabled(false);
         String solution =game.getSolution();
         for (int i=0;i<NUM_PEGS;i++) {
            int color = Character.getNumericValue((solution.charAt(i)));
               history[0][i].setBackground(colors[color]);
               history[0][i].setBorderPainted(false);
               history[0][i].setFocusPainted(false);
               history[0][i].setOpaque(true);

         }
      }
   }
   private class CloseListener implements ActionListener {
      public void actionPerformed(ActionEvent e){
         makeGuess.setEnabled(false);
            for (int j=0;j< NUM_PEGS;j++){
               inputButtons[j].setBackground(Color.white);
         }
            gameIsOver();
      }
   }
   private class NewListener implements ActionListener {
      public void actionPerformed(ActionEvent e){
         for (int i=0;i< game.getNumGuesses();i++){
            for (int j=0;j< NUM_PEGS;j++){
               history[i][j].setBackground(Color.gray);
               history[i][j].setBorderPainted(false);
               history[i][j].setFocusPainted(false);
               history[i][j].setOpaque(true);
               response[i][j].setText(" ");
            }
         }
         column=0;
         makeGuess.setEnabled(true);
         game = new MMGame(NUM_PEGS, NUM_COLORS);
      }
   }
   /*

   */
   int column=0;
   private class MakeGuessListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         String guess="";
         for (int i=0;i<NUM_PEGS;i++){
            int color=findBackGround(inputButtons[i].getBackground());
            guess=guess+color;
         }
         boolean valid=game.guess(guess);
         if (valid==true) {
            for (int i=0;i<NUM_PEGS;i++){
               inputButtons[i].setBackground(colors[0]);
               inputButtons[i].setBorderPainted(false);
               inputButtons[i].setFocusPainted(false);
               inputButtons[i].setOpaque(true);
            }

            for (int i=0;i<NUM_PEGS;i++) {
               int color=Character.getNumericValue((guess.charAt(i)));
               if (game.isGameOver()==false) {
                  history[column][i].setBackground(colors[color]);
                  history[column][i].setBorderPainted(false);
                  history[column][i].setFocusPainted(false);
                  history[column][i].setOpaque(true);
               }

            }
            int partial= game.numPartial();
            int complete= game.numComplete();
            for (int i=0;i<NUM_PEGS;i++){
               if (complete>0) {
                  response[column][i].setText("O");
                  response[column][i].setForeground(Color.black);
                  response[column][i].setOpaque(true);
                  complete--;
               }
               else if(partial>0){
                  response[column][i].setText("O");
                  response[column][i].setForeground(Color.white);
                  response[column][i].setOpaque(true);
                  partial--;
               }
            }
            column++;
            if (game.isGameOver()==true){
               JOptionPane.showMessageDialog(null,"congratulations you won!");
               makeGuess.setEnabled(false);
               gameIsOver();
            }
            else if(game.isGameOver()==false&& game.getNumGuesses()==MAX_GUESSES){
               JOptionPane.showMessageDialog(null,"you lose");
               makeGuess.setEnabled(false);
               gameIsOver();
            }
         }
      }
   }
   private class InputButtonListener  implements ActionListener{
      public void actionPerformed(ActionEvent e){
         JButton source= (JButton) e.getSource();
         Color color = source.getBackground();
         //System.out.println(color);
         int foundColor=(findBackGround(color)+1)%NUM_COLORS;
         source.setBackground(colors[foundColor]);
         source.setBorderPainted(false);
         source.setFocusPainted(false);
         source.setOpaque(true);
         //System.out.println(colors[foundColor]);


         //System.out.println("Button item input Button is not implemented");
      }
   }
   private int findBackGround(Color c){
      int position=0;
      for (int i=0;i< colors.length;i++){
         if (c.equals(colors[i])){
            position=i;
         }
      }
      return position;
   }
   private void gameIsOver(){
      for (int i=0;i< game.getNumGuesses();i++){
         for (int j=0;j< NUM_PEGS;j++){
            history[i][j].setBackground(Color.GRAY);
            history[i][j].setBorderPainted(false);
            history[i][j].setFocusPainted(false);
            history[i][j].setOpaque(true);
            response[i][j].setText(" ");
         }
      }
   }

}