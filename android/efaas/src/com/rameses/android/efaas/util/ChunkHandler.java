package com.rameses.android.efaas.util;

public abstract interface ChunkHandler
{
  public abstract void start();
  
  public abstract void end();
  
  public abstract void handle(int paramInt, byte[] paramArrayOfByte);
}
