/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.service.eb;

import java.util.ArrayList;
import java.util.List;
import org.jlab.clas.reco.ReconstructionEngine;
import org.jlab.detector.base.DetectorType;
import org.jlab.io.base.DataEvent;
import org.jlab.io.evio.EvioDataBank;



/**
 *
 * @author gavalian
 */
public class EBEngine extends ReconstructionEngine {
    
    public EBEngine(){
        super("EB","gavalian","1.0");
    }
    
    @Override
    public boolean processDataEvent(DataEvent de) {
        
        int eventType = EBio.TRACKS_HB;
        
//        if(EBio.isTimeBased(de)==true){
//            eventType = EBio.TRACKS_TB;
//        }
        
        DetectorEvent  detectorEvent = new DetectorEvent();
        
        List<DetectorParticle>  chargedParticles = EBio.readTracks(de, eventType);
        List<DetectorResponse>  ftofResponse     = EBio.readFTOF(de);
        List<DetectorResponse>  ecalResponse     = EBio.readECAL(de);
        List<CherenkovSignal> htccSignal = EBio.readHTCC(de);

        
        EBProcessor processor = new EBProcessor(chargedParticles);
        processor.addTOF(ftofResponse);
        processor.addECAL(ecalResponse);
        processor.addHTCC(htccSignal);
        
        processor.matchTimeOfFlight();
        processor.matchCalorimeter();
        processor.matchHTCC();
        processor.matchNeutral();
        
        for(int i = 0 ; i < chargedParticles.size() ; i++){  //Organize all particles into detectorEvent
            detectorEvent.addParticle(chargedParticles.get(i));
            //System.out.println(chargedParticles.get(i).getNphe("htcc"));
        }

      
        EventTrigger trigger = new EventTrigger();  
        
        List<Integer> usertriggerids= new ArrayList<Integer>();//User can request trigger
        usertriggerids.add(0,11); 
        
        trigger.setUserTriggerIDs(usertriggerids);
        detectorEvent.setEventTrigger(trigger);
        
        EBTrigger.GetFinalTriggerInformation(detectorEvent); //Identifies Trigger Particle and Start Time
        EBTrigger.CalcBeta(detectorEvent);//Calculates Speed of Each Track
        

        if(EBio.isTimeBased(de)==true){
           EBPID.doTimeBasedPID(detectorEvent);
        }
        
        /*
        if(de.hasBank("RUN::config")==true){
            EvioDataBank bankHeader = (EvioDataBank) de.getBank("RUN::config");
            System.out.println(String.format("***>>> RUN # %6d   EVENT %6d", 
                    bankHeader.getInt("Run",0), bankHeader.getInt("Event",0)));
        }*/
        //processor.show();
        /*System.out.println("CHARGED PARTICLES = " + chargedParticles.size());
        for(org.jlab.clas.ebuilder.DetectorParticle p : chargedParticles){
            System.out.println(p);
        }*/
        EvioDataBank pBank = (EvioDataBank) EBio.writeTraks(processor.getParticles(), eventType);
        de.appendBanks(pBank);
        
        return true;
    }

    @Override
    public boolean init() {
        System.out.println("[EB::] --> event builder is ready....");
        return true;
    }
    
}
