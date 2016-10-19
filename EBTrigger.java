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

import org.jlab.detector.base.DetectorType;
import static java.lang.Math.abs;

/**
 *
 * @author jnewton
 */
public class EBTrigger {
        
    public static void GetFinalTriggerInformation(DetectorEvent event) {
           
        for(int i = 0 ; i < event.getParticles().size() ; i++) {
            TIDResult result = new TIDResult();
            result = EBTrigger.GetShoweringParticleEvidence(event.getParticles().get(i)); //How much does it resemble electron/positron?
            event.getParticles().get(i).setScore(result.getScore());//"score" is recorded for each DetectorParticle
        }
     
        ElectronTriggerList electron = new ElectronTriggerList();
        HashMap<Integer,DetectorParticle> ElectronCandidates = electron.getCandidates(event);//Possible Candidates for Electrons
        event.getEventTrigger().setElectronCandidates(ElectronCandidates);
            
        PositronTriggerList positron = new PositronTriggerList();
        HashMap<Integer,DetectorParticle> PositronCandidates = positron.getCandidates(event);//Possible Candidates for Positrons
        event.getEventTrigger().setPositronCandidates(PositronCandidates);
            
        boolean TriggerExists = false;
        if(ElectronCandidates.size()>0 && TriggerExists==false){
           TriggerElectron Te = new TriggerElectron();
           Te.CollectBestTriggerInfo(event); //calculate start time and obtain other relevant trigger information based off "best" electron
           TriggerExists = true;
        }
            
        if(PositronCandidates.size()>0 && TriggerExists==false){
            TriggerPositron Teplus = new TriggerPositron();
            Teplus.CollectBestTriggerInfo(event); //calculate start time and obtain other relevant trigger information based off "best" positron
        }
            
            
    }

       
     
    public static void CalcBeta2(DetectorParticle p, DetectorEvent e){
        if(p.hasHit(DetectorType.FTOF, 2)==true){
            org.jlab.clas.detector.DetectorResponse res = p.getHit(DetectorType.FTOF, 2);
            double path = res.getPath();
            double time = res.getTime();
            double beta = path/(time-e.getEventTrigger().getStartTime())/29.9792; //betas that will be sent to time-based tracking
          //double beta = path/time/29.9792;
          //  System.out.println(e.getEventTrigger().getStartTime());
            p.setBeta(beta);
            double mom = p.vector().mag();
            double mass2 = (mom*mom - beta*beta*mom*mom)/(beta*beta);
            p.setMass(mass2);
        }
    }
    
    public static void CalcBeta(DetectorEvent e){
        for(DetectorParticle p : e.getParticles()){
            EBTrigger.CalcBeta2(p,e);
        }
    }
       
    


    
    public static TIDResult GetShoweringParticleEvidence(DetectorParticle particle) {  
               
        TIDExamination TID = new TIDExamination(); //This is the "DetectorParticle"s PID properties.
        TID.setCorrectSF(TID.SamplingFractionCheck(particle)); //Is the sampling fraction within +-5 Sigma?
        TID.setHTCC(TID.HTCCSignal(particle)); //Is there a signal in HTCC?
        TID.setFTOF(particle.hasHit(DetectorType.FTOF, 2));//Is there a hit in FTOF1B?
               
        TIDResult Result = new TIDResult();
        Result.setScore(TID.getTriggerScore());//Trigger Score for Electron/Positron
        Result.setTIDExamination(TID);
                
           
        return Result;
      }
    
        
}


class TriggerElectron implements BestTrigger {
    
    public void CollectBestTriggerInfo(DetectorEvent event){ //Obtain trigger information with electron
                   EventTrigger Trigger = new EventTrigger();
                   Trigger = event.getEventTrigger();
                   DetectorParticle BestTrigger = Trigger.GetBestTriggerParticle(Trigger.getElectronCandidates());
                   Trigger.setTriggerParticle(BestTrigger);
                   Trigger.setStartTime(Trigger.StartTime(BestTrigger,11));
                   Trigger.setzt(BestTrigger.vertex().z());
                   
    }
}

class TriggerPositron implements BestTrigger {
    
    public void CollectBestTriggerInfo(DetectorEvent event){ //Obtain trigger information with positron
                   EventTrigger Trigger = new EventTrigger();
                   Trigger = event.getEventTrigger();
                   DetectorParticle BestTrigger = Trigger.GetBestTriggerParticle(Trigger.getPositronCandidates());
                   Trigger.setTriggerParticle(BestTrigger);
                   Trigger.setStartTime(Trigger.StartTime(BestTrigger,11));
                   Trigger.setzt(BestTrigger.vertex().z());
                   
    }
}



class ElectronTriggerList implements TriggerCandidateList {//Compiles a map of DetectorParticles, which could possibly be electrons 
    public HashMap<Integer,DetectorParticle>  getCandidates(DetectorEvent event) {
        HashMap<Integer,DetectorParticle> map = new HashMap<Integer,DetectorParticle>();
            int mapiteration = 0;
            for(int i = 0 ; i < event.getParticles().size() ; i++){
                if(event.getParticles().get(i).getCharge()==-1 && event.getParticles().get(i).getNphe("htcc")>0
                        && event.getParticles().get(i).hasHit(DetectorType.FTOF, 2)){
                    map.put(mapiteration,event.getParticles().get(i));
                    mapiteration = mapiteration + 1;
                }
            }
        return map;
    }
}

class PositronTriggerList implements TriggerCandidateList {//Compiles a map of DetectorParticles, which could possibly be positrons
    public HashMap<Integer,DetectorParticle>  getCandidates(DetectorEvent event) {
        HashMap<Integer,DetectorParticle> map = new HashMap<Integer,DetectorParticle>();
            int mapiteration = 0;
            for(int i = 0 ; i < event.getParticles().size() ; i++){
                if(event.getParticles().get(i).getCharge()==1 && event.getParticles().get(i).getNphe("htcc")>0
                        && event.getParticles().get(i).hasHit(DetectorType.FTOF, 2)){
                    map.put(mapiteration,event.getParticles().get(i));
                    mapiteration = mapiteration + 1;
                }
            }
        return map;
    }
}
