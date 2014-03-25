package jp.argos.ESZ;

import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;


public class EmulateSuperZoom implements PlugIn{
	
	int mDefWidth;
	int mDefHeight;

	@Override
	public void run(String arg) {
		ImagePlus imp = IJ.getImage();
		if(imp == null){
			IJ.error("Image not found. error.");
			return;
		}
		
		ImageStack iStack = imp.getImageStack();
		int stacksize = imp.getImageStackSize();
		
		IJ.log("ImageStack size is "+stacksize);
		double root = Math.sqrt(stacksize);
		int rootint = (int)root;
		double dif = (double)rootint - root;
		if(dif != 0){
			IJ.error("number of stack is not square.");
			IJ.log("number of stack is not square.");
			return;
		}
		IJ.log("sqrt of stacksize is "+ rootint);
		
		mDefWidth = imp.getWidth();
		mDefHeight = imp.getHeight();
		int szWidth = mDefWidth * rootint;
		int szHeight = mDefHeight * rootint;
		IJ.log("sz width : "+szWidth);
		ImagePlus szimp = IJ.createImage("SuperZoomImage", szWidth, szHeight, 1, 24);
		szimp.setColor(new Color(255,0,0));
		ImageProcessor szip = szimp.getProcessor();
		
		for(int i = 1; i <= stacksize; i++){
			ImageProcessor ip = iStack.getProcessor(i);
			int dX = i%rootint - 1;
			int dY = i/rootint;
			
			
			for(int y = 1; y <= szHeight; y++){
				for(int x = 1; x <= szWidth; x++){
					int[] rgb = new int[3];
					ip.getPixel(x, y, rgb);
					szip.putPixel(x * rootint - dX, y * rootint - dY, rgb);
				}
			}
		}
		szimp.show();
		IJ.showMessage("Process finish!");
	}

}
