/*
 * TaskManager.java
 *
 * Created on January 27, 2014, 3:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import java.util.Timer;

/**
 *
 * @author wflores
 */
public class TaskManager 
{
    private Timer timer; 
    
    TaskManager() {
        timer = new Timer();
    }
    
    synchronized void close() {
        try { timer.cancel(); } catch(Throwable t){;} 
        try { timer.purge(); } catch(Throwable t){;} 
    }

    public void schedule(Runnable runnable) {
        schedule(runnable, 0);
    } 
    
    public void schedule(Runnable runnable, long delay) {
        schedule(new TaskRunnableProxy(runnable), delay);
    } 
    
    public void schedule(Runnable runnable, long delay, long period) {
        schedule(new TaskRunnableProxy(runnable), delay, period);
    } 

    public void schedule(Task task) {
        schedule(task, 0);
    } 
    
    public void schedule(Task task, long delay) {
        schedule(task, delay, 0);
    } 
    
    public void schedule(Task task, long delay, long period) {
        if (period <= 0) { 
            timer.schedule(task, delay); 
        } else { 
            timer.schedule(task, delay, period); 
        } 
    } 
    
    
    
    private class TaskRunnableProxy extends Task 
    {
        private Runnable runnable;
        
        TaskRunnableProxy(Runnable runnable) {
            this.runnable = runnable; 
        }

        public void run() { 
            try { 
                runnable.run(); 
            } catch(Throwable t) {
                t.printStackTrace(); 
            }
        } 
    } 
}
