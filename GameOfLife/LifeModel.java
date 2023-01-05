import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.awt.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.Timer;

public class LifeModel implements ActionListener
{

	/*
	 *  This is the Model component.
	 */

	private static int SIZE = 60;
	private LifeCell[][] grid;
	
	LifeView myView;
	Timer timer;

	Scanner console = new Scanner(System.in);

	/** Construct a new model using a particular file */
	public LifeModel(LifeView view, String fileName) throws IOException
	{       
		int r, c;
		grid = new LifeCell[SIZE][SIZE];
		for ( r = 0; r < SIZE; r++ )
			for ( c = 0; c < SIZE; c++ )
				grid[r][c] = new LifeCell();

		if ( fileName == null ) //use random population
		{                                           
			for ( r = 0; r < SIZE; r++ )
			{
				for ( c = 0; c < SIZE; c++ )
				{
					if ( Math.random() > 0.85) //15% chance of a cell starting alive
						grid[r][c].setAliveNow(true);
				}
			}
		}
		else
		{                 
			Scanner input = new Scanner(new File(fileName));
			int numInitialCells = input.nextInt();
			for (int count=0; count<numInitialCells; count++)
			{
				r = input.nextInt();
				c = input.nextInt();
				grid[r][c].setAliveNow(true);
			}
			input.close();
		}

		myView = view;
		myView.updateView(grid);

	}

	/** Constructor a randomized model */
	public LifeModel(LifeView view) throws IOException
	{
		this(view, null);
	}

	/** pause the simulation (the pause button in the GUI */
	public void pause()
	{
		timer.stop();
	}
	
	/** resume the simulation (the pause button in the GUI */
	public void resume()
	{
		timer.restart();
	}
	
	/** run the simulation (the pause button in the GUI */
	public void run()
	{
		timer = new Timer(50, this);
		timer.setCoalesce(true);
		timer.start();
	}

	public void reset() throws FileNotFoundException {
		System.out.println("Would you like to input a file? (0 for yes) (1 for no)");
		int choice = console.nextInt();
		console.nextLine();
		int r,c;

		if(choice == 0)
		{
			System.out.println("Input the filename >>> ");
			String fileName = console.nextLine();

			Scanner input = new Scanner(new File(fileName));
			int numInitialCells = input.nextInt();
			for (int count=0; count<numInitialCells; count++)
			{
				r = input.nextInt();
				c = input.nextInt();
				grid[r][c].setAliveNow(true);
			}
			input.close();
		} else{
			for ( r = 0; r < SIZE; r++ )
			{
				for ( c = 0; c < SIZE; c++ )
				{
					if ( Math.random() > 0.85) //15% chance of a cell starting alive
						grid[r][c].setAliveNow(true);
				}
			}
		}
		myView.updateView(grid);
	}

	private boolean p = false;
	public void randomizeColor() throws Exception
	{
		p = true;

		Thread t = new Thread(){
			public void run(){
				while(p){
					Random rand = new Random();
					float s = rand.nextFloat();
					float a = rand.nextFloat();
					float b = rand.nextFloat();
					Color randomColor = new Color(s, a, b);
					//System.out.println("Color:"+randomColor.getRGB());
					try{Thread.sleep(5);}catch(InterruptedException e){System.out.println(e);}
					myView.updateViewColor(randomColor);
				}

			}
		};
		t.start();




//		for ( int r = 0; r < SIZE; r++ )
//		{
//			for (int c = 0; c < SIZE; c++ )
//			{
//				if (grid[r][c] != null)
//				{
//					if ( grid[r][c].isAliveNow() )
//						g.setColor( randomColor);
//					else
//						g.setColor( new Color(235,235,255) );
//
//					g.fillRect( (c+1)*boxSize, (r+1)* boxSize, boxSize-2, boxSize-2);
//				}
//			}
//		}

	}

	public void resetColor()
	{
		myView.updateViewColor(new Color(0, 0, 255));
	}

	public void stopRandom()
	{
		p = false;
	}
	/** called each time timer fires */
	public void actionPerformed(ActionEvent e)
	{
		oneGeneration();
		myView.updateView(grid);
	}

	/** main logic method for updating the state of the grid / simulation */
	private void oneGeneration()
	{
		 LifeCell[][] gen0 = grid;

		 for(int i = 0; i < gen0.length; i++)
		 {
		 	for(int c = 0; c < gen0[i].length;c++)
			{
				int count = 0;
				int size = gen0.length - 1;
				boolean con = false;

				//All 4 corners
				if(i == 0 && c == 0)	//top Left Corner
				{
					if(gen0[i+1][c].isAliveNow())
						count++;
					if(gen0[i+1][c+1].isAliveNow())
						count++;
					if(gen0[i][c+1].isAliveNow())
						count++;
					con = true;
				}

				if(i == size && c == 0 )	//bottom left corner
				{
					if(gen0[i-1][c].isAliveNow())
						count++;
					if(gen0[i-1][c+1].isAliveNow())
						count++;
					if(gen0[i][c+1].isAliveNow())
						count++;
					con = true;
				}

				if(i == size && c == size) //Bottom right Corner
				{
					if(gen0[i-1][c].isAliveNow())
						count++;
					if(gen0[i-1][c-1].isAliveNow())
						count++;
					if(gen0[i][c-1].isAliveNow())
						count++;
					con = true;
				}

				if(i == 0 && c == size) //top right Corner
				{
					if(gen0[i+1][c].isAliveNow())
						count++;
					if(gen0[i+1][c-1].isAliveNow())
						count++;
					if(gen0[i][c-1].isAliveNow())
						count++;
					con = true;
				}

				//All 4 edges
				if(c > 0 && c < size && i == 0) //Top edge
				{
					if(gen0[i+1][c+1].isAliveNow())
						count++;
					if(gen0[i][c+1].isAliveNow())
						count++;
					if(gen0[i+1][c].isAliveNow())
						count++;
					if(gen0[i][c-1].isAliveNow())
						count++;
					if(gen0[i+1][c-1].isAliveNow())
						count++;
					con = true;
				}

				if(c > 0 && c < size&& i == size) // Bottom edge
				{
					if(gen0[i-1][c].isAliveNow())
						count++;
					if(gen0[i-1][c+1].isAliveNow())
						count++;
					if(gen0[i][c+1].isAliveNow())
						count++;
					if(gen0[i-1][c-1].isAliveNow())
						count++;
					if(gen0[i][c-1].isAliveNow())
						count++;
					con = true;
				}

				if(i > 0 && i < size && c == 0) //left edge
				{
					if(gen0[i+1][c].isAliveNow())
						count++;
					if(gen0[i+1][c+1].isAliveNow())
						count++;
					if(gen0[i][c+1].isAliveNow())
						count++;
					if(gen0[i-1][c+1].isAliveNow())
						count++;
					if(gen0[i-1][c].isAliveNow())
						count++;
					con = true;
				}

				if(i > 0 && i < size && c == size) //right edge
				{
					if(gen0[i+1][c].isAliveNow())
						count++;
					if(gen0[i+1][c-1].isAliveNow())
						count++;
					if(gen0[i][c-1].isAliveNow())
						count++;
					if(gen0[i-1][c-1].isAliveNow())
						count++;
					if(gen0[i-1][c].isAliveNow())
						count++;
					con = true;
				}

				//Everything else
				//System.out.println(count);
				if(count == 0 && !con)
				{
					if(gen0[i+1][c].isAliveNow())
						count++;
					//System.out.println((i+1) + " " + (c-1));
					if(gen0[i+1][c-1].isAliveNow())
						count++;
					if(gen0[i+1][c+1].isAliveNow())
						count++;
					if(gen0[i][c-1].isAliveNow())
						count++;
					if(gen0[i][c+1].isAliveNow())
						count++;
					//System.out.println((i-1) + " " + c);
					if(gen0[i-1][c].isAliveNow())
						count++;
					if(gen0[i-1][c-1].isAliveNow())
						count++;
					if(gen0[i-1][c+1].isAliveNow())
						count++;
				}

				//See if cell is alive
				if(count == 0 || count == 1 || count == 4 || count == 5 || count == 6 || count == 7 || count == 8)
				{
					grid[i][c].setAliveNow(false);
				} else {
					if(count == 3)
					{
						grid[i][c].setAliveNow(true);

					}
				}
				//ystem.out.println("Row: " + i + " || Cell: " + c);
			}
		 }
	}
}
