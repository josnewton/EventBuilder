/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.service.eb;

import java.io.PrintWriter;
import static java.lang.Math.abs;
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
    
    public boolean processDataEvent(DataEvent de) {
        
        int eventType = EBio.TRACKS_HB;
        
        if(EBio.isTimeBased(de)==true){
            eventType = EBio.TRACKS_TB;
        }
        
        
        List<DetectorParticle>  chargedParticles = EBio.readTracks(de, eventType);
        List<org.jlab.clas.detector.DetectorResponse>  ftofResponse     = EBio.readFTOF(de);
        List<org.jlab.clas.detector.DetectorResponse>  ecalResponse     = EBio.readECAL(de);
        List<CherenkovSignal> htccSignal = EBio.readHTCC(de);

        
        EBProcessor processor = new EBProcessor(chargedParticles);
        processor.addTOF(ftofResponse);
        processor.addECAL(ecalResponse);
        processor.addHTCC(htccSignal);
        
        processor.matchTimeOfFlight();
        processor.matchCalorimeter();
        processor.matchHTCC();
        processor.matchNeutral();
        
        DetectorEvent  detectorEvent = new DetectorEvent();
        
        for(int i = 0 ; i < chargedParticles.size() ; i++){ 
            detectorEvent.addParticle(chargedParticles.get(i));
        }

      
        EventTrigger trigger = new EventTrigger();  
        detectorEvent.setEventTrigger(trigger);
        
        EBTrigger triggerinfo = new EBTrigger();
        triggerinfo.setEvent(detectorEvent);
        triggerinfo.setDataEvent(de);
        
        triggerinfo.RFInformation(); //Obtain RF Time
        triggerinfo.Trigger();//Use Trigger Particle Vertex Time and RF Time for Start Time
        triggerinfo.CalcBetas(); //Calculate Speeds and Masses of Particles
       
        //System.out.println(detectorEvent.getEventTrigger());
        
        
        EBPID pid = new EBPID();
        if(EBio.isTimeBased(de)==true){
           pid.setEvent(detectorEvent);
           pid.PIDAssignment();//PID Assignment
              }
        

        
        for(int i = 0 ; i < detectorEvent.getParticles().size(); i++){
           System.out.println("Particle ID " + detectorEvent.getParticles().get(i).getPid());
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
//        EvioDataBank pBank = (EvioDataBank) EBio.writeTraks(processor.getParticles(), eventType);
 //       de.appendBanks(pBank);
        
        return true;
    }

    @Override
    public boolean init() {
        System.out.println("[EB::] --> event builder is ready....");
        return true;
    }


    
}
