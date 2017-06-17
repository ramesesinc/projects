package com.rameses.android.efaas.util;

public abstract class AbstractChunkHandler
implements ChunkHandler
{
private FileObject.MetaInfo meta;
private boolean cancelled;

public boolean isCancelled()
{
  return this.cancelled;
}

public void cancel()
{
  this.cancelled = true;
}

public boolean isAutoComputeTotals()
{
  return true;
}

public FileObject.MetaInfo getMeta()
{
  return this.meta;
}

void setMeta(FileObject.MetaInfo meta)
{
  this.meta = meta;
}

public void start() {}

public void end() {}
}
