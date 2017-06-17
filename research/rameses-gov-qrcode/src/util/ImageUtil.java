package util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtil {
    
    public static byte[] scale(byte[] data, int width, int height){
        ByteArrayInputStream in = new ByteArrayInputStream(data);
    	try {
    		BufferedImage img = ImageIO.read(in);
    		Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    		BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    		imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);
    		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    		ImageIO.write(imageBuff, "jpg", buffer);
    		return buffer.toByteArray();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
        return null;
    }
    
    public static byte[] convertToBytes(BufferedImage image){
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", os);
            return os.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            try {
                os.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    
}
