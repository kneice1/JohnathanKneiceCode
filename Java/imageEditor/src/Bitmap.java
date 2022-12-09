import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Bitmap {

    /**
     * private static class to hold pixel color information
     */
    private static class Color {
        int blue;
        int green;
        int red;

        /**
         * Default Constructor for private class Color
         */
        public Color(int r, int g, int b) {
            red = r;
            green = g;
            blue = b;
        }
    }


    private int width;             // width of image in pixels
    private int height;            // height of image in pixels
    private Color[][] colors;      // array of pixel colors

    /**
     * Will read in 4 bytes from file and convert from little endian to integer
     */
    private static int readInt(FileInputStream file) throws IOException {
        // Base 8 not base 10
        int value = file.read();
        value = value + (file.read() << 8);
        value = value + (file.read() << 16);
        value = value + (file.read() << 24);
        return value;
    }

    /**
     * will output an integer in little endian to file
     */
    private static void writeInt(FileOutputStream file, int value) throws IOException {
        file.write(value & 0xff);
        file.write((value >>> 8) & 0xff);
        file.write((value >>> 16) & 0xff);
        file.write((value >>> 24) & 0xff);
    }

    /**
     * will output a short in little endian to file
     */
    private static void writeShort(FileOutputStream file, int value) throws IOException {
        file.write(value & 0xff);
        file.write((value >>> 8) & 0xff);
    }

    /**
     * Default Constructor for class Bitmap
     */
    public Bitmap(FileInputStream file) throws IOException {
        //From bitmap header we want the width and the height, so we skip everything else
        file.skip(18);
        width = readInt(file);
        height = readInt(file);
        file.skip(28);
        //Every line has padding which will need to be skipped, this determines what amount needs to be skipped
        int padding = 3*width % 4;
        if (padding > 0) {
            padding = 4 - padding;
        }
        // Using height, width, and padding we will read in the color information from the file
        colors = new Color[height][width];
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                colors[i][j] = new Color(0,0,0);
                colors[i][j].blue = file.read();
                colors[i][j].green = file.read();
                colors[i][j].red = file.read();
            }
            file.skip(padding);
        }

    }

    /**
     * Function to output bitmap onto file
     */
    public void output(FileOutputStream file) throws IOException {
        //Some bitmaps need to be padded when being output, here we determine how much padding we need to output
        int padding = 3*width % 4;
        if (padding > 0) {
            padding = 4 - padding;
        }
        int dataSize = height * ((width * 3) + padding);
        // output header. Most of the values being output here are constant
        file.write('B');
        file.write('M');
        writeInt(file, 54 + dataSize + 2);
        writeInt(file, 0);
        writeInt(file, 54);
        writeInt(file, 40);
        writeInt(file, width);
        writeInt(file, height);
        writeShort(file, 1);
        writeShort(file, 24);
        writeInt(file, 0);
        writeInt(file, dataSize);
        writeInt(file, 72);
        writeInt(file, 72);
        writeInt(file, 0);
        writeInt(file, 0);
        //output the color data into bitmap file while adding the padding required
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                file.write(colors[i][j].blue);
                file.write(colors[i][j].green);
                file.write(colors[i][j].red);

            }
            for (int k = 0; k < padding; k++) {
                file.write(0);
            }
        }
        file.write(0);
        file.write(0);

    }

    /** invertBitmap will invert the colors of the image*/
    //take 255 and subtract it by the value of green,red and blue.
    public void invertBitmap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                colors[i][j].red = 255 - colors[i][j].red;
                colors[i][j].blue = 255 - colors[i][j].blue;
                colors[i][j].green = 255 - colors[i][j].green;
            }
        }
    }

    /** grayScaleBitmap will convert the image into grayscale*/
    //Set the R, G, and B values for a single
    //pixel to one value. That value should be: .3R + .59G + .11B, rounded to the nearest integer.
    public void grayScaleBitmap() {
        int gray;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gray = (int) Math.round(colors[i][j].red * .3 + colors[i][j].blue * .11 + colors[i][j].green * .59);
                colors[i][j].red = gray;
                colors[i][j].green = gray;
                colors[i][j].blue = gray;
            }
        }
    }

    /** blurBitmap will blur the image*/
    public void blurBitmap() {
        Color[][] blurColors = new Color[height][width];
        //obtain the color data of a 5 x 5 pixel square then average the values of them
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = 0;
                int green = 0;
                int blue = 0;
                int count = 0;
                // using max and min to take care of the edge cases
                for (int row = Math.max(i - 2,0); row <= Math.min(i + 2,height-1); row++) {
                    for (int column = Math.max(j - 2,0); column <= Math.min(j + 2,width-1); column++) {
                        red += colors[row][column].red;
                        green += colors[row][column].green;
                        blue += colors[row][column].blue;
                        count++;
                    }

                }
                //taking the average of RGB and then putting them into the blurColors array
                red = (int) Math.round(red / (double) count);
                green = (int) Math.round(green / (double) count);
                blue = (int) Math.round(blue / (double) count);
                blurColors[i][j] = new Color(red, green, blue);
            }
        }
        colors=blurColors;
    }

    /**Vertically Mirrors the Bitmap*/
    public void flipBitmap(){
        for(int i=0; i<height/2; i++){
            for(int j=0; j<width; j++){
                Color flipColor = new Color(0,0,0);
                flipColor=colors[(height-1)-i][j];
                colors[(height-1)-i][j]=colors[i][j];
                colors[i][j]=flipColor;
            }
        }

    }

    /**Rotates Bitmap by 90 degrees*/
    public void rotateBitmap(){
        Color[][] rotatedColor = new Color[width][height];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                rotatedColor[j][(height-1)-i] = new Color(colors[i][j].red, colors[i][j].green, colors[i][j].blue);
            }
        }
        colors=rotatedColor;
        int temp = width;
        width=height;
        height=temp;
    }

    /**Doubles Bitmap*/
    public void doubleBitmap(){
        Color[][] doubleColor = new Color[height*2][width*2];
        //offsets are needed in order to input the color data into the correct pixels
        int rowOffset=0;
        for(int i=0; i<height; i++){
            int columnOffset=0;
            for(int j=0; j<width; j++){
                doubleColor[i+rowOffset][j+columnOffset]=new Color(colors[i][j].red, colors[i][j].green, colors[i][j].blue);
                doubleColor[i+rowOffset+1][j+columnOffset]=new Color(colors[i][j].red, colors[i][j].green, colors[i][j].blue);
                doubleColor[i+rowOffset][j+columnOffset+1]=new Color(colors[i][j].red, colors[i][j].green, colors[i][j].blue);
                doubleColor[i+rowOffset+1][j+columnOffset+1]=new Color(colors[i][j].red, colors[i][j].green, colors[i][j].blue);
                columnOffset++;
            }
            rowOffset++;
        }
        colors=doubleColor;
        height=height*2;
        width=width*2;
    }
    /**Shrinks Bitmap*/
    public void shrinkBitmap(){
        height=height/2;
        width=width/2;
        Color[][] halveColor = new Color[height][width];


        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                /** average 4 pixels of the colors red, green and blue */
                int red = (int)Math.round((colors[i*2][j*2].red+colors[i*2+1][j*2].red+colors[i*2][j*2+1].red+colors[i*2+1][j*2+1].red)/4.0);
                int green = (int)Math.round((colors[i*2][j*2].green+colors[i*2+1][j*2].green+colors[i*2][j*2+1].green+colors[i*2+1][j*2+1].green)/4.0);
                int blue =(int)Math.round((colors[i*2][j*2].blue+colors[i*2+1][j*2].blue+colors[i*2][j*2+1].blue+colors[i*2+1][j*2+1].blue)/4.0);
                /** making a new color of the average of the 4 pixels colors
                 * setting a pixel's color equal to the new color*/
                halveColor[i][j] = new Color(red,green,blue);
            }

        }
        colors=halveColor;

    }
}

