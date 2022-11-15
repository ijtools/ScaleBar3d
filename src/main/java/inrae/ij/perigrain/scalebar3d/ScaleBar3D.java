/**
 * 
 */
package inrae.ij.perigrain.scalebar3d;

import ij.ImageStack;

/**
 * Gathers all the information necessary to draw a 3D scale bar onto a 3D image.
 * 
 * @author dlegland
 *
 */
public class ScaleBar3D
{
    /**
     * The different possible orientations of the scale bar.
     */
    public enum Orientation
    {
        X_AXIS(1,0,0),
        Y_AXIS(0,1,0),
        Z_AXIS(0,0,1);
        
        int dirX;
        int dirY;
        int dirZ;
        
        private Orientation(int dx, int dy, int dz)
        {
            this.dirX = dx;
            this.dirY = dy;
            this.dirZ = dz;
        }
        
        public int dirX()
        {
            return dirX;
        }
        
        public int dirY()
        {
            return dirY;
        }
        
        public int dirZ()
        {
            return dirZ;
        }
    }
    
    /**
     * The different possible positions of the scale bar.
     * 
     * As the scale bar is close to a corner, we identify the corner from the
     * positions along each coordinate.
     */
    public enum Location
    {
        FRONT_TOP_LEFT(0, 0, 0),
        FRONT_TOP_RIGHT(1, 0, 0),
        FRONT_BOTTOM_LEFT(0, 1, 0),
        FRONT_BOTTOM_RIGHT(1, 1, 0),
        BACK_TOP_LEFT(0, 0, 1),
        BACK_TOP_RIGHT(1, 0, 1),
        BACK_BOTTOM_LEFT(0, 1, 1),
        BACK_BOTTOM_RIGHT(1, 1, 1);
        
        int posX;
        int posY;
        int posZ;
        
        private Location(int px, int py, int pz)
        {
            this.posX = px;
            this.posY = py;
            this.posZ = pz;
        }
        
        public int posX()
        {
            return posX;
        }
        
        public int posY()
        {
            return posY;
        }
        
        public int posZ()
        {
            return posZ;
        }
    }
    
    /** The length of the scale bar, in voxels. */
    int length;
    
    /** The radius of the scale bar, in voxels. */
    int radius;
    
    /**
     * The orientation of the scale bar.
     */
    Orientation orientation;

    /**
     * The position of the scale bar.
     */
    Location position;
    
    int spacing;
    
    
    public ScaleBar3D(int length, int radius, Orientation orientation)
    {
        this(length, radius, orientation, Location.BACK_BOTTOM_RIGHT);
    }

    public ScaleBar3D(int length, int radius, Orientation orientation, Location position)
    {
        this(length, radius, orientation, position, 10);
    }

    public ScaleBar3D(int length, int radius, Orientation orientation, Location position, int spacing)
    {
        this.orientation = orientation;
        this.length = length;
        this.radius = radius;
        this.position = position;
        this.spacing = spacing;
    }
    
    public void draw(ImageStack stack)
    {
        switch(orientation)
        {
            case X_AXIS: drawAlongX(stack); break;
            case Y_AXIS: drawAlongY(stack); break;
            case Z_AXIS: drawAlongZ(stack); break;
        }
    }
    
    private void drawAlongX(ImageStack stack)
    {
        int sizeX = stack.getWidth();
        int sizeY = stack.getHeight();
        int sizeZ = stack.getSize();
        
        int x0 = position.posX == 0 ? spacing : sizeX - length - spacing;
        int y0 = position.posY == 0 ? spacing + radius : sizeY - radius - spacing;
        int z0 = position.posZ == 0 ? spacing + radius : sizeZ - radius - spacing;
        
        // iterate over the length of the scale bar
        double r2 = (radius + 0.5) * (radius + 0.5);
        for (int ix = 0; ix < length; ix++)
        {
            int x = Math.min(x0 + ix, sizeX - 1);
            
            for (int iz = -radius; iz <= radius; iz++)
            {
                int z = Math.min(z0 + iz, sizeZ - 1);
                for (int iy = -radius; iy <= radius; iy++)
                {
                    int y = Math.min(y0 + iy, sizeY - 1);
                    
                    if (iy * iy + iz * iz < r2)
                    {
                        stack.setVoxel(x, y, z, 255);
                    }
                }
            }
        }
    }
    
    private void drawAlongY(ImageStack stack)
    {
        int sizeX = stack.getWidth();
        int sizeY = stack.getHeight();
        int sizeZ = stack.getSize();
        
        int x0 = position.posX == 0 ? spacing + radius : sizeX - radius - spacing;
        int y0 = position.posY == 0 ? spacing : sizeY - length - spacing;
        int z0 = position.posZ == 0 ? spacing + radius : sizeZ - radius - spacing;
        
        // iterate over the length of the scale bar
        double r2 = (radius + 0.5) * (radius + 0.5);
        for (int iy = 0; iy < length; iy++)
        {
            int y = Math.min(y0 + iy, sizeY - 1);
            
            for (int iz = -radius; iz <= radius; iz++)
            {
                int z = Math.min(z0 + iz, sizeZ - 1);
                for (int ix = -radius; ix <= radius; ix++)
                {
                    int x = Math.min(x0 + ix, sizeX - 1);
                    
                    if (ix * ix + iz * iz < r2)
                    {
                        stack.setVoxel(x, y, z, 255);
                    }
                }
            }
        }
    }
    
    private void drawAlongZ(ImageStack stack)
    {
        int sizeX = stack.getWidth();
        int sizeY = stack.getHeight();
        int sizeZ = stack.getSize();
        
        int x0 = position.posX == 0 ? spacing + radius : sizeX - radius - spacing;
        int y0 = position.posY == 0 ? spacing + radius : sizeY - radius - spacing;
        int z0 = position.posZ == 0 ? spacing : sizeZ - length - spacing;
        
        // iterate over the length of the scale bar
        double r2 = (radius + 0.5) * (radius + 0.5);
        for (int iz = 0; iz < length; iz++)
        {
            int z = Math.min(z0 + iz, sizeZ - 1);
            
            for (int iy = -radius; iy <= radius; iy++)
            {
                int y = Math.min(y0 + iy, sizeY - 1);
                for (int ix = -radius; ix <= radius; ix++)
                {
                    int x = Math.min(x0 + ix, sizeX - 1);
                    
                    if (ix * ix + iy * iy < r2)
                    {
                        stack.setVoxel(x, y, z, 255);
                    }
                }
            }
        }
    }
}
