package com.rameses.android.efaas.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileChunker
{
  private int chunkSize = 32000;
  private Parser parser = null;
  
  public FileChunker(File file)
  {
    this.parser = new FileParser(file);
  }
  
  public FileChunker(byte[] bytes, String name, String type)
  {
    this.parser = new ByteParser(bytes, null, null);
  }
  
  public int getChunkSize()
  {
    return this.chunkSize;
  }
  
  public void setChunkSize(int chunkSize)
  {
    this.chunkSize = chunkSize;
  }
  
  public String getName()
  {
    return this.parser.getName();
  }
  
  public String getType()
  {
    return this.parser.getType();
  }
  
  public long getLength()
  {
    return this.parser.getLength();
  }
  
  public int getCount()
  {
    long len = getLength();
    long count = len / getChunkSize();
    if (len % getChunkSize() > 0L) {
      count += 1L;
    }
    return (int)count;
  }
  
  public List<byte[]> getChunks()
  {
    ChunkHandlerImpl handler = new ChunkHandlerImpl();
    parse(handler);
    return handler.results;
  }
  
  public void parse(ChunkHandler handler)
  {
    this.parser.parse(handler);
  }
  
  private class ChunkHandlerImpl
    extends AbstractChunkHandler
  {
    List<byte[]> results = new ArrayList();
    
    private ChunkHandlerImpl() {}
    
    public void start()
    {
      this.results.clear();
    }
    
    public void end() {}
    
    public void handle(int indexno, byte[] bytes)
    {
      this.results.add(bytes);
    }
  }
  
  private static abstract interface Parser
  {
    public abstract String getName();
    
    public abstract String getType();
    
    public abstract long getLength();
    
    public abstract void parse(ChunkHandler paramChunkHandler);
  }
  
  private class FileParser
    implements FileChunker.Parser
  {
    File file;
    
    FileParser(File file)
    {
      this.file = file;
    }
    
    public String getName()
    {
      return this.file.getName();
    }
    
    public String getType()
    {
      try
      {
        URLConnection urlconn = this.file.toURL().openConnection();
        return urlconn.getContentType();
      }
      catch (RuntimeException re)
      {
        throw re;
      }
      catch (Exception e)
      {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
    
    public long getLength()
    {
      FileInputStream fis = null;
      FileChannel fc = null;
      byte[] bytes = null;
      try
      {
        fis = new FileInputStream(this.file);
        fc = fis.getChannel();
        return fc.size();
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
          fc.close();
        }
        catch (Throwable t) {}
        try
        {
          fis.close();
        }
        catch (Throwable t) {}
      }
    }
    
    public void parse(ChunkHandler handler)
    {
      FileInputStream fis = null;
      FileChannel fc = null;
      try
      {
        fis = new FileInputStream(this.file);
        fc = fis.getChannel();
        
        handler.start();
        
        int counter = 1;int bytesRead = -1;
        ByteBuffer buffer = ByteBuffer.allocate(FileChunker.this.getChunkSize());
        while ((bytesRead = fc.read(buffer)) != -1)
        {
          buffer.flip();
          if (buffer.hasRemaining())
          {
            byte[] chunks = buffer.array();
            if (bytesRead < chunks.length)
            {
              byte[] dest = new byte[bytesRead];
              System.arraycopy(chunks, 0, dest, 0, bytesRead);
              handler.handle(counter, dest);
            }
            else
            {
              handler.handle(counter, chunks);
            }
            counter++;
          }
          buffer.clear();
        }
        handler.end(); return;
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
          fc.close();
        }
        catch (Throwable ign) {}
        try
        {
          fis.close();
        }
        catch (Throwable ign) {}
      }
    }
  }
  
  private class ByteParser
    implements FileChunker.Parser
  {
    byte[] bytes;
    String name;
    String type;
    
    ByteParser(byte[] bytes, String name, String type)
    {
      this.bytes = bytes;
      this.name = name;
      this.type = type;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public String getType()
    {
      return this.type;
    }
    
    public long getLength()
    {
      return this.bytes.length;
    }
    
    public void parse(ChunkHandler handler)
    {
      ByteArrayInputStream bais = null;
      BufferedInputStream bis = null;
      try
      {
        bais = new ByteArrayInputStream(this.bytes);
        bis = new BufferedInputStream(bais);
        
        handler.start();
        
        int counter = 1;int read = -1;
        byte[] chunks = new byte[FileChunker.this.getChunkSize()];
        while ((read = bis.read(chunks)) != -1)
        {
          if (read < chunks.length)
          {
            byte[] dest = new byte[read];
            System.arraycopy(chunks, 0, dest, 0, read);
            handler.handle(counter, dest);
          }
          else
          {
            handler.handle(counter, chunks);
          }
          chunks = new byte[FileChunker.this.getChunkSize()];
          counter++;
        }
        handler.end(); return;
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
          bis.close();
        }
        catch (Throwable ign) {}
        try
        {
          bais.close();
        }
        catch (Throwable ign) {}
      }
    }
  }
}
