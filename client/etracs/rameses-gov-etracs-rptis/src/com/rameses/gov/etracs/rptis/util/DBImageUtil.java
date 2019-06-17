/*
 * DBImageUtil.java
 * Created on February 25, 2014, 10:58 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.gov.etracs.rptis.util;

import com.rameses.common.MethodResolver;
import com.rameses.io.StreamUtil;
import com.rameses.osiris2.client.InvokerProxy;
import com.rameses.osiris2.client.OsirisContext;

import groovy.lang.GroovyClassLoader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class DBImageUtil {

    private final int BUFFER_SIZE = 32768;
    private static DBImageUtil util;
    private static String serviceName = "DBImageService";
    private MethodResolver resolver;
    private Object proxy;
    private GroovyClassLoader classLoader = new GroovyClassLoader(getClass().getClassLoader());
    private Date lastErrorConnectionTime;
    
    public static final String CACHE_IMAGE_KEY = "cached_image_file";

    private DBImageUtil(String serviceName) {
        reconnect();
    }

    private void reconnect() {
        long diff = 0;
        long diffMinutes = 0;

        Date currTime = new Date();
        if (lastErrorConnectionTime != null) {
            diff = currTime.getTime() - lastErrorConnectionTime.getTime();
            diffMinutes = diff / (60 * 1000) % 60;
        }


        // try reconnect after 5 minutes
        if (lastErrorConnectionTime == null || diffMinutes >= 1.0) {
            System.out.println("Reconnecting to Image Server... ");
            try {
                proxy = lookupServiceProxy(serviceName);
                resolver = MethodResolver.getInstance();
            } catch (Exception ex) {
                System.out.println("===============================================");
                System.out.println("Unable to reconnect to Image Server...");
                System.out.println("===============================================");
                ex.printStackTrace();
                lastErrorConnectionTime = currTime;
            }
        }
    }

    private Object lookupServiceProxy(String name) {
        try {
            return InvokerProxy.getInstance().create(name);
        } catch (Exception e) {
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private interface ScriptInfoInf {

        String getStringInterface();
    }

    public static DBImageUtil getInstance() {
        return getInstance(serviceName);
    }

    public static DBImageUtil getInstance(String svcName) {
        serviceName = svcName;
        if (util == null) {
            util = new DBImageUtil(serviceName);
        }
        return util;
    }

    String getCacheDirectory() {
        return System.getProperty("user.dir") + File.separatorChar + "cache";
    }

    String getCacheDirectory(String parentFolder) {
        String path = System.getProperty("user.dir") + File.separatorChar + "cache";
        if (parentFolder != null) {
            path += File.separatorChar + parentFolder;
        }
        return path;
    }

    String getFullFileName(Object objid) {
        // String safeid = makeSafeId(objid);
        // return getCacheDirectory() + File.separatorChar + safeid;
        return getFullFileName(null, objid);
    }

    String getFullFileName(String parentFolder, Object objid) {
        String safeid = makeSafeId(objid);
        String path = getCacheDirectory() + File.separatorChar;
        if (parentFolder != null) {
            path += parentFolder + File.separatorChar;
        }
        return path + safeid;
    }

    String makeSafeId(Object objid) {
        return objid.toString().replaceAll(":", "-");
    }

    public byte[] getImage(Object objid) throws Exception {
        clearCacheDir(getCacheDirectory());
        String filename = getFullFileName(objid);
        File file = new File(filename);
        
        if (file.length() == 0){
            file.delete();
            System.out.println("File deleted.");
        }
        
        if (!file.exists()) {
            System.out.println("Saving " + filename + " ");
            saveToFile(objid, filename);
            file = new File(filename);
        }
        
        return StreamUtil.toByteArray(new FileInputStream(file));
    }

    public File getImage2(Object objid) throws Exception {
        return getImage2(null, objid);
        // clearCacheDir(getCacheDirectory());
        // String filename = getFullFileName(objid);

        // File file = new File(filename);
        // if (!file.exists()) {
        //     System.out.println("Saving " + filename + " ");
        //     saveToFile(objid, filename);
        //     file = new File(filename);
        // }
        // return file;
    }

    public File getImage2(String parentFolder, Object objid) throws Exception {
        clearCacheDir(getCacheDirectory(parentFolder));
        String filename = getFullFileName(parentFolder, objid);

        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Saving " + filename + " ");
            saveToFile(objid, filename);
            file = new File(filename);
        }
        return file;
    }
    
    public InputStream getInputStream(Object objid) throws Exception{
        File file = getImage2(objid);
        return new FileInputStream(file);
    }

    public long saveClipboardImage(Map header) {
        try {
            byte[] bytes = getClipboardImage();
            if (bytes != null){
                InputStream is = new ByteArrayInputStream(bytes);
                return upload(header, is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public byte[] getClipboardImage() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                BufferedImage bufferedImage = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
                ImageIcon img = new ImageIcon(bufferedImage);
                BufferedImage bimage = convertImage(img.getImage());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bimage, "png", baos);
                return baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("getImageFromClipboard: That wasn't an image!");
        }
        return null;
    }

    private BufferedImage convertImage(Image img) {
        if (img == null) {
            return null;
        }
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        // draw original image to thumbnail image object and 
        // scale it to the new size on-the-fly 
        BufferedImage bufimg = new BufferedImage(w, h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufimg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, Color.WHITE, null);
        g2.dispose();
        return bufimg;
    }

    public void deleteCachedImage(Object objid) {
        String filename = getFullFileName(objid);
        File file = new File(filename);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("Cannot delete file at this time.");
            }
        }
    }

    void clearCacheDir(String cacheDir) {
        File file = new File(cacheDir);
        if (!file.exists()) {
            file.mkdir();
        } else {
            if (System.getProperty(CACHE_IMAGE_KEY) == null) {
                System.out.println("Clearing image cache -> " + cacheDir);
                File[] files = file.listFiles();
                for (File f : files) {
                    f.delete();
                }
            }
            System.getProperties().put(CACHE_IMAGE_KEY, "initialized");
        }
    }

    public void saveToFile(Object objid, String fileName) throws Exception {
        FileOutputStream fos = null;
        long fileSize = 0;

        try {
            File file = new File(fileName);
            fos = new FileOutputStream(file, true);
            List<Map> imageItems = getImageItems(objid);
            for (Map data : imageItems) {
                byte[] bytes = (byte[]) data.get("byte");
                fileSize += bytes.length;
                fos.write(bytes);
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
    }
    
    
    
    public InputStream saveImageToFile(Object objid, byte[] image) throws Exception {
        clearCacheDir(getCacheDirectory());
        String filename = getFullFileName(objid);
        FileOutputStream fos = null;
        
        File file = new File(filename);
        boolean success = false;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(image);
            fos.flush();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
        
        if (success)
            return new FileInputStream(file);
        return null;
    }

    public long upload(Map header, String filename) throws Exception {
        InputStream is = null;
        BufferedInputStream bis = null;
        long filesize = 0;
        File file = new File(filename);
        try {
            header.put("filesize", file.length());
            is = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            filesize = upload(header, bis);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {
                }
            };
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            };
        }
        if (filesize != file.length()) {
            throw new Exception("Unload unsuccessful. Try again.");
        }
        return file.length();
    }

    public long upload(Map header, InputStream source) throws Exception {
        final byte[] buf = new byte[BUFFER_SIZE];
        long filesize = 0;
        int len = -1;
        int fileno = 0;

        if (header.get("objid") == null) {
            header.put("objid", "H" + new java.rmi.server.UID());
        }

        try {
            deleteImage(header.get("objid"));
            saveHeader(header);
            while ((len = source.read(buf)) != -1) {
                fileno += 1;
                filesize += len;

                Map data = new HashMap();
                data.put("objid", "F" + new java.rmi.server.UID());
                data.put("parentid", header.get("objid"));
                data.put("fileno", fileno);
                data.put("byte", buf);
                saveItem(data);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        return filesize;
    }

    public long upload(Map header, byte[] bytes) throws Exception {
        final byte[] buf = new byte[BUFFER_SIZE];
        int fileno = 0;

        if (header.get("objid") == null) {
            header.put("objid", "H" + new java.rmi.server.UID());
        }

        try {
            deleteImage(header.get("objid"));
            saveHeader(header);

            int startidx = fileno * BUFFER_SIZE;
            int endidx = startidx + BUFFER_SIZE;
            long filesize = 0;

            while (endidx <= bytes.length) {
                System.arraycopy(bytes, startidx, buf, 0, BUFFER_SIZE);
                fileno += 1;
                startidx = fileno * BUFFER_SIZE;
                endidx = startidx + BUFFER_SIZE;
                filesize += BUFFER_SIZE;

                Map data = new HashMap();
                data.put("objid", "F" + new java.rmi.server.UID());
                data.put("parentid", header.get("objid"));
                data.put("fileno", fileno);
                data.put("byte", buf);
                saveItem(data);
            }
            if (filesize < bytes.length) {
                System.arraycopy(bytes, startidx, buf, 0, (int) (bytes.length - filesize));
                fileno += 1;
                Map data = new HashMap();
                data.put("objid", "F" + new java.rmi.server.UID());
                data.put("parentid", header.get("objid"));
                data.put("fileno", fileno);
                data.put("byte", buf);
                saveItem(data);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        return bytes.length;
    }

    public void deleteImage(Object objid) {
        deleteCachedImage(objid);

        if (isConnectionActive() == false) {
            throw new RuntimeException("Image Server is not available at this time. Try again later.");
        }
        Map data = new HashMap();
        data.put("objid", objid);
        invoke("deleteImage", data);
    }

    public void deleteAllImages(Object refid) {
        if (isConnectionActive() == false) {
            throw new RuntimeException("Image Server is not available at this time. Try again later.");
        }
        Map data = new HashMap();
        data.put("refid", refid);
        invoke("deleteAllImages", data);
    }

    public List<Map> getImages(Object objid) {
        List<Map> list = new ArrayList<Map>();
        if (isConnectionActive()) {
            Map data = new HashMap();
            data.put("refid", objid);
            list = (List<Map>) invoke("getImages", data);
        } else {
            reconnect();
        }
        return list;
    }

    private boolean isConnectionActive() {
        if (proxy == null) {
            return false;
        }
        return true;
    }

    private List<Map> getImageItems(Object objid) {
        List<Map> list = new ArrayList<Map>();
        if (isConnectionActive()) {
            Map data = new HashMap();
            data.put("objid", objid);
            list = (List<Map>) invoke("getImageItems", data);
        }
        return list;
    }

    private Object saveItem(Map data) {
        if (isConnectionActive() == false) {
            throw new RuntimeException("Image Server is not available at this time. Try again later.");
        }
        return invoke("saveItem", data);
    }

    private Object saveHeader(Map data) {
        if (isConnectionActive() == false) {
            throw new RuntimeException("Image Server is not available at this time. Try again later.");
        }
        return invoke("saveHeader", data);
    }

    private Object invoke(String methodName, Object data) {
        try {
            return resolver.invoke(proxy, methodName, new Object[]{data});
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception re) {
            throw new RuntimeException(re.getMessage(), re);
        }
    }

    private Map getParams() {
        Map appEnv = OsirisContext.getClientContext().getAppEnv();

        Object appHost = appEnv.get("image.server.host");
        Object appContext = appEnv.get("image.server.context");
        Object appCluster = appEnv.get("image.server.cluster");

        if (appHost == null) {
            appHost = appEnv.get("app.host");
            appContext = appEnv.get("app.context");
            appCluster = appEnv.get("app.cluster");
        }

        System.out.println("=====================================");
        System.out.println("image.server.host -> " + appHost);
        System.out.println("image.server.context -> " + appContext);
        System.out.println("image.server.cluster -> " + appCluster);
        System.out.println("=====================================");


        Map param = new HashMap();
        param.put("app.host", appHost);
        param.put("app.context", appContext);
        param.put("app.cluster", appCluster);
        return param;
    }
}
