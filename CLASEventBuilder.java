/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clas.ebuilder;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.jlab.clas.ebuilder.EventBuilderDetectors;
import org.jlab.clas.ebuilder.EventBuilderMatching;
import org.jlab.clas.ebuilder.EBPID;
import org.jlab.clas.ebuilder.EventBuilderParticles;
import org.jlab.clas.physics.GenericKinematicFitter;

import org.jlab.clas.physics.PhysicsEvent;

import org.jlab.clas.physics.Particle;
import org.jlab.detector.base.DetectorType;
import org.jlab.geom.prim.Vector3D;
import org.jlab.io.evio.EvioDataBank;
import org.jlab.io.evio.EvioDataEvent;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;




/**
 *
 * @author gavalian
 */
public class CLASEventBuilder {
//public class CLASEventBuilder extends DetectorReconstruction {
  //  private GenericKinematicFitter fitter = new GenericKinematicFitter(11.0,"11:X+:X-:Xn");
    private Integer debugMode = 0;
  //  public CLASEventBuilder() {
  //      super("EB", "gavalian", "1.0");
  //  }

   // @Override
	public void processEvent(EvioDataEvent event) {
          double gp = 0.0, theta = 0.0, phi=0.0;

         
            
        GenericKinematicFitter fitter = new GenericKinematicFitter(11.0);   
      //  PhysicsEvent  genEvent  = fitter.getGeneratedEvent(event);
      //  Particle j1 = genEvent.getParticle("[11]");
       
        DetectorEvent  detectorEvent = EventBuilderParticles.getDetectorEvent(event);
        
        List<DetectorResponse>  detectorResponses = EventBuilderDetectors.getDetectorResponses(event);
        List<CherenkovSignal> cherenkovSignals = EventBuilderDetectors.getCherenkovSignals(event);
        
        int nparticles = detectorEvent.getParticles().size();
        for(int i = 0; i < nparticles; i++){
            EventBuilderMatching.particleMatchTOF(
						  detectorEvent.getParticles().get(i), i, 
						  detectorResponses);
            EventBuilderMatching.particleMatchEC( detectorEvent.getParticles().get(i), i,detectorResponses);
            EventBuilderMatching.particleMatchHTCC(detectorEvent.getParticles().get(i), i, cherenkovSignals);
        }
        
        List<DetectorParticle> neutrals = EventBuilderMatching.createNeutralParticles(detectorResponses);
        for(DetectorParticle n : neutrals){
            detectorEvent.addParticle(n);
        }
        
        nparticles = detectorEvent.getParticles().size();
        for(int i = 0; i < nparticles; i++){        
            if(detectorEvent.getParticles().get(i).getStatus()==300){                    
                EventBuilderMatching.particleMatchNeutral(detectorEvent.getParticles().get(i), i,detectorResponses);
            }
        }
        

        EventTrigger trigger = new EventTrigger();
      //  trigger = EventBuilderInformation.GetInitialTriggerInformation(event);
       
       
     
        List<Integer> usertriggerids= new ArrayList<Integer>();
        usertriggerids.add(0,11);
        usertriggerids.add(1,-11);
        usertriggerids.add(2,22);
        
        trigger.setUserTriggerIDs(usertriggerids);
        detectorEvent.setEventTrigger(trigger);
        
        
  //     EventBuilderInformation.GetFinalTriggerInformation(detectorEvent);//Obtains start time (RF corrected) and records the trigger particle and its identification

       EBTrigger.GetFinalTriggerInformation(detectorEvent);

       DetectorParticle tp = new DetectorParticle();
       tp = detectorEvent.getEventTrigger().getTriggerParticle();
       if(detectorEvent.getEventTrigger().getStartTime()!=0.0){
       System.out.println(detectorEvent.getEventTrigger().getStartTime());
       }
      /* if(detectorEvent.getEventTrigger().getTriggerParticle().hasHit(DetectorType.EC,1)){
       System.out.println("PCAL DOCA (cm) = " + tp.getResponse(DetectorType.EC, 1).getDistance(tp).length());
       }
       if(detectorEvent.getEventTrigger().getTriggerParticle().hasHit(DetectorType.EC,4)){
       System.out.println("ECINNER DOCA (cm) = " + tp.getResponse(DetectorType.EC, 4).getDistance(tp).length());
       }
       if(detectorEvent.getEventTrigger().getTriggerParticle().hasHit(DetectorType.EC,7)){
       System.out.println("ECOUTER DOCA (cm) = " + tp.getResponse(DetectorType.EC, 7).getDistance(tp).length());
       }
       String str = "htcc";
       System.out.println("HTCC # of Photoelectrons = " + detectorEvent.getEventTrigger().getTriggerParticle().getNphe(str));
       System.out.println("Sampling Fraction = " + detectorEvent.getEventTrigger().getTriggerParticle().CalculatedSF());
       System.out.println("Trigger ID Score For Showering Particles = " + detectorEvent.getEventTrigger().getTriggerParticle().getTIDResult().getScore());
       */
      
  //    EventBuilderPID.doMass(detectorEvent);
  //    EventBuilderPID.doTimeBasedPID(detectorEvent);

      
    /*   nparticles = detectorEvent.getParticles().size();
        for(int i = 0; i < nparticles; i++){        
                
                if(detectorEvent.getParticles().get(i).getPIDResult().getFinalID()==11){
                System.out.println("Final ID " + detectorEvent.getParticles().get(i).getPIDResult().getFinalID());
                break;
                }

        
        }
    */
        
      //  System.out.println(detectorEvent.toString());


      // System.out.println("RESPONSES SIZE = " + detectorResponses.size());
        for(DetectorResponse res : detectorResponses){            
       //     System.out.println(res);
              }
        
        //EventBuilderPID.doMass(detectorEvent);
        
        
        
      //  EventBuilderPID.doTimeBasedPID(detectorEvent);
        


        
     //   EvioDataBank  bankP = EventBuilderParticles.createBank(detectorEvent);
     //   EvioDataBank  bankD = EventBuilderDetectors.createBank(detectorResponses);
     //   event.appendBanks(bankP,bankD);
        //EventBuilderStore store = new EventBuilderStore();
        /*
        if(event.hasBank("DC::dgtz")==true){
            System.out.println(" found DC::dgtz");
        }
        if(event.hasBank("TimeBasedTrkg::TBTracks")==true){ 
            System.out.println(" found TRACKING banks");
	    }*/
        
        /*
        store.initForwardParticles(event);
        store.initCentralParticles(event);
        store.initDetectorResponses(event);
        store.doDetectorMatching();
        */
        /*
        if(this.debugMode>0){
            System.out.println("******************>>>>>> DETECTOR EVENT ");
            System.out.println(store.getDetectorEvent().toString());
        }
        
        if(this.debugMode>1){
            System.out.println("******************>>>>>> DETECTOR RESPONSE STORE ");
            store.show();
	    }*/
        /*
        PhysicsEvent  genEvent = this.fitter.getGeneratedEvent(event);        
        System.out.println(genEvent.toLundString());
        System.out.println(store.getDetectorEvent());
        store.show();
        */
        /*
        EvioDataBank bank = store.getParticleBank(event);
        event.appendBanks(bank);
        */
        
    }
/*
    @Override
	public void init() {
        this.requireGeometry("EC");
        this.requireGeometry("FTOF");        
    }

    @Override
	public void configure(ServiceConfiguration c) {
        if(c.hasItem("SEB","debug")==true){
            this.debugMode = Integer.parseInt(c.asString("SEB", "debug"));
            
            System.out.println("[SEB] Initializing debugging mode = " 
			       + this.debugMode + "  string " + c.asString("SEB", "debug")
			       );
        }
    }
*/
    
}