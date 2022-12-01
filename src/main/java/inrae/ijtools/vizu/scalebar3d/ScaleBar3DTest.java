/**
 * 
 */
package inrae.ijtools.vizu.scalebar3d;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ij.ImageStack;
import inrae.ijtools.vizu.scalebar3d.ScaleBar3D.Location;
import inrae.ijtools.vizu.scalebar3d.ScaleBar3D.Orientation;

/**
 * @author dlegland
 *
 */
public class ScaleBar3DTest
{
    /**
     * Test method for {@link inrae.ijtools.vizu.scalebar3d.ScaleBar3D#draw(ij.ImageStack)}.
     */
    @Test
    public final void testDraw()
    {
        ImageStack stack = ImageStack.create(100, 100, 100, 8);
        ScaleBar3D scaleBar = new ScaleBar3D(20, 2, Orientation.X_AXIS, Location.FRONT_TOP_LEFT, 10);
        
        scaleBar.draw(stack);
        
        assertEquals(  0.0, stack.getVoxel( 9, 12, 12), 0.1);
        assertEquals(255.0, stack.getVoxel(10, 12, 12), 0.1);
        
        assertEquals(255.0, stack.getVoxel(29, 12, 12), 0.1);
        assertEquals(  0.0, stack.getVoxel(30, 12, 12), 0.1);

        assertEquals(255.0, stack.getVoxel(10, 10, 12), 0.1);
        assertEquals(255.0, stack.getVoxel(10, 14, 12), 0.1);
        assertEquals(255.0, stack.getVoxel(10, 12, 10), 0.1);
        assertEquals(255.0, stack.getVoxel(10, 12, 14), 0.1);
        
        assertEquals(255.0, stack.getVoxel(29, 10, 12), 0.1);
        assertEquals(255.0, stack.getVoxel(29, 14, 12), 0.1);
        assertEquals(255.0, stack.getVoxel(29, 12, 10), 0.1);
        assertEquals(255.0, stack.getVoxel(29, 12, 14), 0.1);
    }

//    /**
//     * Test method for {@link inrae.ij.perigrain.scalebar3d.ScaleBar3D#draw(ij.ImageStack)}.
//     */
//    @Test
//    public final void testDraw()
//    {
//        fail("Not yet implemented"); // TODO
//    }

}
