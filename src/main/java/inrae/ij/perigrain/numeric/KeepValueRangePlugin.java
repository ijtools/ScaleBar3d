/**
 * 
 */
package inrae.ij.perigrain.numeric;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

/**
 * @author dlegland
 *
 */
public class KeepValueRangePlugin implements PlugIn
{

    @Override
    public void run(String arg)
    {
        // retrieve current image
        ImagePlus imagePlus = IJ.getImage();
        
        // create the dialog, with operator options
        GenericDialog gd = new GenericDialog("Keep Value Range");
        gd.addNumericField("Min Value",   0, 0);
        gd.addNumericField("Max Value", 255, 0);
        gd.showDialog();
        
        // If cancel was clicked, do nothing
        if (gd.wasCanceled())
            return;
        
        // retrieve user options
        double minValue = gd.getNextNumber();
        double maxValue = gd.getNextNumber();
        
        // process image, either as a stack or as a 2D image
        ImagePlus resPlus = imagePlus.duplicate();
        if (resPlus.getStackSize() > 1)
        {
            // process stack
            ImageStack stack = resPlus.getStack();
            for (int iSlice = 0; iSlice  < resPlus.getStackSize(); iSlice++)
            {
                IJ.showProgress(iSlice, stack.getSize());
                ImageProcessor image = stack.getProcessor(iSlice+1);
                process(image, minValue, maxValue);
                stack.setProcessor(image, iSlice+1);
            }
        }
        else
        {
            // process planar image
            ImageProcessor image = resPlus.getProcessor();
            process(image, minValue, maxValue);
            resPlus.setProcessor(image);
        }
        
        // display result
        resPlus.setTitle(imagePlus.getShortTitle() + "-keepValues");
        resPlus.show();
    }
    
    private void process(ImageProcessor image, double minValue, double maxValue)
    {
        int sizeX = image.getWidth();
        int sizeY = image.getHeight();
        
        for (int y = 0; y < sizeY; y++)
        {
            for (int x = 0; x < sizeX; x++)
            {
                image.setf(x, y, (float) process(image.getf(x, y), minValue, maxValue));
            }
        }
    }
    
    /**
     * Replaces value by zero if it is out of range
     * 
     * @param val
     *            the value to process
     * @return the same value if it is comprised between minVal and maxVal, and
     *         zero otherwise.
     */
    private static final double process(double val, double minValue, double maxValue)
    {
        if (val < minValue) return 0.0;
        if (val <= maxValue) return val;
        return 0.0;
    }
}
