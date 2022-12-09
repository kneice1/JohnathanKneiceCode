import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;



public class Manipulator {

    public static void main(String[] args) throws IOException {
        System.out.print("What image file would you like to edit: ");
        Scanner in = new Scanner(System.in);
        String fileName= in.next();
        Bitmap bitmap = null;
        FileInputStream file = null;

    // Opens bitmap and puts it into the Bitmap object
        try {
             file = new FileInputStream(fileName);
            bitmap = new Bitmap(file);

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            file.close();
        }

        String userChoice = "";
        while (!userChoice.equals("q")){
            System.out.print("What command would you like to perform (i, g, b, v, s, d, r, or q): ");
            userChoice = in.next();

            switch(userChoice){
                case "i": bitmap.invertBitmap(); break;
                case "g": bitmap.grayScaleBitmap(); break;
                case "b": bitmap.blurBitmap(); break;
                case "v": bitmap.flipBitmap(); break;
                case "d":bitmap.doubleBitmap(); break;
                case "r":bitmap.rotateBitmap(); break;
                case "s":bitmap.shrinkBitmap(); break;
            }
        }





        System.out.print("What do you want to name your new image file: ");
        Scanner out = new Scanner(System.in);
        fileName= in.next();
        FileOutputStream file2 = null;
        //Outputs Bitmap
        try {

            file2 = new FileOutputStream(fileName);
            bitmap.output(file2);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            file2.close();
        }
    }






}



