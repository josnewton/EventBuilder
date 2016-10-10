/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;




import org.jlab.io.base.DataEvent;
import org.jlab.io.evio.EvioSource;




/**
 *
 * @author jnewt018
 */
public class eb {
   
        public static void main(String[] args)  {
      

        EvioSource reader = new EvioSource();
        reader.open("/Users/jnewt018/Desktop/ParticleID/electronbelowth.0.evio");
       

       
       
       
           EBEngine evnt = new EBEngine();
   

        while(reader.hasEvent()==true){
          System.out.println("New Event");
           // System.out.println("Event #  " + eventcounter);
           // System.out.println(eventcounter);
            DataEvent  event = (DataEvent) reader.getNextEvent();
        evnt.processDataEvent(event);
         
 
    }
//System.out.println(jos);
    
    
}
   
}