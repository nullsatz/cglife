import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.Random;
import java.util.Vector;

public class Life {

	public int nrows;
	public int ncols;
	public Vector<int[][]> history;

	public Life(int initRows, int initCols) {
		nrows = initRows;
		ncols = initCols;

		history = new Vector<int[][]>(1000);
		history.add(new int[nrows][ncols]);

		int[][] seedFrame = history.firstElement();
		Random tileGen = new Random();
		for(int i = 0; i < nrows; i++) {
			for(int j = 0; j < ncols; j++)
				seedFrame[i][j] = tileGen.nextInt(2);
		}
	}

	public int[][] getNborCoords(int i, int j) {
		int[][] nborCoords = {
			{i - 1, j - 1}, {i - 1, j}, {i - 1, j + 1},
			{i, j - 1}, {i, j + 1},
			{i + 1, j - 1}, {i + 1, j}, {i + 1, j + 1},
		};
		
		int lastRow = nrows - 1;
		int lastCol = ncols - 1;
		
		if(i == 0) {
			for(int k = 0; k < 3; k++)
				nborCoords[k][0] = lastRow;
		} else if(i == lastRow) { 
			for(int k = 5; k < 8; k++)
				nborCoords[k][0] = 0;
		}
		
		if(j == 0) {
			nborCoords[0][1] = lastCol;
			nborCoords[3][1] = lastCol;
			nborCoords[5][1] = lastCol;
		} else if(j == lastCol) {
			nborCoords[2][1] = 0;
			nborCoords[4][1] = 0;
			nborCoords[7][1] = 0;
		}
		return(nborCoords);
	}

	public int getNborSum(int i, int j) {
		int[][] coords = getNborCoords(i, j);
		int[][] frame = history.lastElement();
		int sum = 0;
		for(int[] coord : coords)
			sum += frame[coord[0]][coord[1]];
		return(sum);
	}

	public int[][] advanceHistory() {
		int[][] newFrame = new int[nrows][ncols];
		int[][] oldFrame = history.lastElement();
		for(int i = 0; i < nrows; i++) {
			for(int j = 0; j < nrows; j++) {
				newFrame[i][j] = oldFrame[i][j];
				int n = getNborSum(i, j);
				if((n < 2) || (n > 3))
					newFrame[i][j] = 0;
				else if(n == 3)
					newFrame[i][j] = 1;
			}
		}
		history.add(newFrame);
		return(newFrame);
	}

	public BufferedImage frameToImage(int time, int[] bg, int[] fg) {
		int[][] frame = history.elementAt(time);
		int[] rgbPixels = new int[(2 * nrows) * (2 * ncols) * 3];
		for(int i = 0; i < nrows; i++) {
			for(int j = 0; j < ncols; j++) {
				int[] pixelColor;
				if(frame[i][j] == 1)
					pixelColor = fg;
				else
					pixelColor = bg;
				int pixelIdx = ((i * ncols) + j) * 3;
				for(int k = 0; k < 3; k++)
					rgbPixels[pixelIdx + k] = pixelColor[k];
			}
		}
		BufferedImage frameImg = new BufferedImage(ncols, nrows,
			BufferedImage.TYPE_INT_RGB);
		frameImg.setRGB(0, 0, ncols, nrows, rgbPixels, 0, ncols);
		return(frameImg);

	}

	public static void main(String[] args)
		throws IOException, InterruptedException {

		int dim = 100;
		int time = 100;
		int pause = 200;

		int[] bg = {0, 0, 0}; // black
		int[] fg =  {255, 0, 0}; // blue

		Life a = new Life(dim, dim);
		for(int i = 1; i < time; i++)
			a.advanceHistory();

		BufferedImage img = a.frameToImage(0, bg, fg);
		Image bigImg = img.getScaledInstance(2 * dim, 2 * dim,
			Image.SCALE_DEFAULT);

		JLabel jl = new JLabel();
		jl.setIcon(new ImageIcon(bigImg));

		JFrame jf = new JFrame();
		jf.add(jl);
		jf.pack();
		jf.setVisible(true);

		for(int i = 1; i < time; i++) {
			img = a.frameToImage(i, bg, fg);
			bigImg = img.getScaledInstance(2 * dim, 2 * dim,
				Image.SCALE_DEFAULT);

			jl.setIcon(new ImageIcon(bigImg));
			jl.paintImmediately(0, 0, 2 * dim, 2 * dim);
			Thread.sleep(pause);
		}
	}
}
