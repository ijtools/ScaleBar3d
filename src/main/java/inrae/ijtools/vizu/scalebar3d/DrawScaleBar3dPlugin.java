/**
 * 
 */
package inrae.ijtools.vizu.scalebar3d;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.Macro;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import inrae.ijtools.vizu.scalebar3d.ScaleBar3D.Location;
import inrae.ijtools.vizu.scalebar3d.ScaleBar3D.Orientation;

/**
 * Draw a 3D scale bar onto the current 3D image.
 * 
 * @author dlegland
 *
 */
public class DrawScaleBar3dPlugin implements PlugIn
{
    static String[] orientationStrings = new String[] {"X-axis", "Y-axis", "Z-axis"};
    
    static String[] locationStrings = new String[] {
            "Front-Top-Left", 
            "Front-Top-Right", 
            "Front-Bottom-Left", 
            "Front-Bottom-Right", 
            "Back-Top-Left", 
            "Back-Top-Right", 
            "Back-Bottom-Left", 
            "Back-Bottom-Right"};
    
    private static int lastLength = 50;
    private static int lastRadius = 5;
    private static String lastOrientationString = orientationStrings[0];
    private static String lastLocationString = locationStrings[7];
    private static int lastSpacing = 10;


    @Override
    public void run(String arg)
    {
        // retrieve current image
        ImagePlus imagePlus = IJ.getImage();
        
        // requires image to be 3D
        if (imagePlus.getStackSize() <= 1)
        {
            IJ.showMessage("Requires a 3D image as input");
            return;
        }
        
        // create the dialog, with operator options
        GenericDialog gd = new GenericDialog("3D Scale Bar");
        gd.addNumericField("Length", lastLength, 0);
        gd.addNumericField("Radius",  lastRadius, 0);
        gd.addChoice("Orientation", orientationStrings, lastOrientationString);
        gd.addChoice("Location", locationStrings, lastLocationString);
        gd.addNumericField("Spacing", lastSpacing, 0);
        gd.showDialog();
        
        // If cancel was clicked, do nothing
        if (gd.wasCanceled())
            return;
        
        // retrieve user options
        int length = (int) gd.getNextNumber();
        int radius = (int) gd.getNextNumber();
        int orientationIndex = gd.getNextChoiceIndex();
        int locationIndex = gd.getNextChoiceIndex();
        int spacing = (int) gd.getNextNumber();
        
        Orientation orientation = Orientation.X_AXIS;
        switch (orientationIndex)
        {
            case 0: orientation = Orientation.X_AXIS; break;
            case 1: orientation = Orientation.Y_AXIS; break;
            case 2: orientation = Orientation.Z_AXIS; break;
            default: throw new RuntimeException("Wrong index of scale bar orientation");
        };
        
        Location location = Location.BACK_BOTTOM_RIGHT;
        switch (locationIndex)
        {
            case 0: location = Location.FRONT_TOP_LEFT; break;
            case 1: location = Location.FRONT_TOP_RIGHT; break;
            case 2: location = Location.FRONT_BOTTOM_LEFT; break;
            case 3: location = Location.FRONT_BOTTOM_RIGHT; break;
            case 4: location = Location.BACK_TOP_LEFT; break;
            case 5: location = Location.BACK_TOP_RIGHT; break;
            case 6: location = Location.BACK_BOTTOM_LEFT; break;
            case 7: location = Location.BACK_BOTTOM_RIGHT; break;
            default: throw new RuntimeException("Wrong index of scale bar location");
        };
        
        // remember parameters for next run
        if (Macro.getOptions() == null)
        {
            lastLength = length;
            lastRadius = radius;
            lastOrientationString = orientationStrings[orientationIndex];
            lastLocationString = locationStrings[locationIndex];
            lastSpacing = spacing;
        }
        
        
        ScaleBar3D scaleBar = new ScaleBar3D(length, radius, orientation, location, spacing);
        
        // add scale bar to image
        ImageStack stack = imagePlus.getStack();
        scaleBar.draw(stack);
        
        imagePlus.updateAndRepaintWindow();
    }
}
