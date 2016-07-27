package com.rameses.android.efaas.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.UUID;

public class FileObject
{
  public static final int CHUNK_SIZE = 65536;
  public static final int MIN_CHUNK_SIZE = 32000;
  private File file;
  private Map info;
  private int chunkSize;
  
  public FileObject(File file)
  {
    this(file, 65536);
  }
  
  public FileObject(File file, int chunkSize)
  {
    this.file = file;
    this.chunkSize = chunkSize;
  }
  
  public File getFile()
  {
    return this.file;
  }
  
  public int getChunkSize()
  {
    return this.chunkSize;
  }
  
  public void setChunkSize(int chunkSize)
  {
    this.chunkSize = chunkSize;
  }
  
  public void read(ChunkHandler handler)
  {
    if (handler == null) {
      throw new NullPointerException("Please specify a ChunkHandler before reading the file ");
    }
    AbstractChunkHandler proxy = null;
    if ((handler instanceof AbstractChunkHandler)) {
      proxy = (AbstractChunkHandler)handler;
    } else {
      proxy = new ChunkHandlerProxy(handler);
    }
    MetaInfo meta = new MetaInfo();
    meta.id = ("FO" + UUID.randomUUID().toString());
    meta.file = this.file;
    try
    {
      URLConnection urlconn = meta.file.toURL().openConnection();
      meta.fileType = urlconn.getContentType();
      meta.fileName = this.file.getName();
    }
    catch (RuntimeException re)
    {
      throw re;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    if (proxy.isAutoComputeTotals())
    {
      meta.autoComputeTotals = true;
      chunk(proxy, meta, true);
    }
    proxy.setMeta(meta);
    proxy.start();
    if (!proxy.isCancelled()) {
      chunk(proxy, meta, false);
    }
    proxy.end();
  }
  
  private void chunk(AbstractChunkHandler handler, MetaInfo meta, boolean bypassHandler)
  {
    int size = getChunkSize();
    if (size < 32000) {
      throw new IllegalStateException("The minimum chunk size is 32kb");
    }
    ByteBuffer buf = ByteBuffer.allocate(size);
    RandomAccessFile raf = null;
    FileChannel channel = null;
    try
    {
      raf = new RandomAccessFile(this.file, "r");
      channel = raf.getChannel();
      if (bypassHandler)
      {
        meta.fileSize = channel.size();
        long num = meta.fileSize / size;
        if (meta.fileSize % size > 0L) {
          num += 1L;
        }
        meta.chunkCount = ((int)num);
      }
      else
      {
        byte[] bytes = null;
        int read = 0;int indexno = 0;
        while ((read = channel.read(buf)) > 0)
        {
          buf.flip();
          bytes = new byte[read];
          System.arraycopy(buf.array(), 0, bytes, 0, read);
          buf.clear();
          
          indexno++;
          if (!meta.autoComputeTotals)
          {
            meta.chunkCount = indexno;
            //MetaInfo.access$514(meta, read);
          }
          handler.handle(indexno, bytes);
          if (handler.isCancelled()) {
            break;
          }
        }
      }
      return;
    }
    catch (RuntimeException re)
    {
      throw re;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    finally
    {
      try
      {
        buf.clear();
      }
      catch (Throwable t) {}
      try
      {
        channel.close();
      }
      catch (Throwable t) {}
      try
      {
        raf.close();
      }
      catch (Throwable t) {}
    }
  }
  
  public class MetaInfo
  {
    private boolean autoComputeTotals;
    private File file;
    private String id;
    private String fileName;
    private String fileType;
    private long fileSize;
    private int chunkCount;
    
    public MetaInfo() {}
    
    public String getId()
    {
      return this.id;
    }
    
    public File getFile()
    {
      return this.file;
    }
    
    public String getFileName()
    {
      return this.fileName;
    }
    
    public String getFileType()
    {
      return this.fileType;
    }
    
    public long getFileSize()
    {
      return this.fileSize;
    }
    
    public int getChunkCount()
    {
      return this.chunkCount;
    }
  }
  
  private class ChunkHandlerProxy
    extends AbstractChunkHandler
  {
    private ChunkHandler handler;
    
    ChunkHandlerProxy(ChunkHandler handler)
    {
      this.handler = handler;
    }
    
    public void start()
    {
      this.handler.start();
    }
    
    public void end()
    {
      this.handler.end();
    }
    
    public void handle(int indexno, byte[] bytes)
    {
      this.handler.handle(indexno, bytes);
    }
  }
}