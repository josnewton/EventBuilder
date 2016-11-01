package org.jlab.clas.ebuilder;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jlab.detector.base.DetectorType;
import static java.lang.Math.abs;
import org.jlab.io.base.DataEvent;
import org.jlab.io.evio.EvioDataBank;

/**
 *
 * @author jnewton
 */
public class EBTrigger {

    DetectorEvent event = new DetectorEvent();
    DataEvent de;

    public EBTrigger(){
        
    }
    
    public void setEvent(DetectorEvent e){this.event = e;}
    public void setDataEvent(DataEvent data){this.de = data;}
    
    public void InitialTriggerInformation() {
      
          if(de.hasBank("RF::info")==true){
            EvioDataBank bank = (EvioDataBank) de.getBank("RF::info");
            event.getEventTrigger().setRFTime(bank.getDouble("rf",0));//Obtain a TDC value
        }
    }
            
        
    public void FinalTriggerInformation() {
            
        for(int i = 0 ; i < this.event.getParticles().size() ; i++) {
            TIDResult result = new TIDResult();
            result = EBTrigger.GetShoweringParticleEvidence(event.getParticles().get(i)); //How much does it resemble electron/positron?
            event.getParticles().get(i).setScore(result.getScore());//"score" is recorded for each DetectorParticle
        }
    
        ElectronTriggerList electron = new ElectronTriggerList();
        PositronTriggerList positron = new PositronTriggerList();
        NegativePionTriggerList negativepion = new NegativePionTriggerList();
        
        HashMap<Integer,DetectorParticle> ElectronCandidates = electron.getCandidates(event);//Possible Candidates for Electrons
        HashMap<Integer,DetectorParticle> PositronCandidates = positron.getCandidates(event);//Possible Candidates for Positrons
        HashMap<Integer,DetectorParticle> NegativePionCandidates = negativepion.getCandidates(event);//Possible Candidates for Negative Pions
        
        event.getEventTrigger().setElectronCandidates(ElectronCandidates);
        event.getEventTrigger().setPositronCandidates(PositronCandidates);
        event.getEventTrigger().setNegativePionCandidates(NegativePionCandidates);
            
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

        if(NegativePionCandidates.size()>0 && TriggerExists==false){
            TriggerNegativePion Piminus = new TriggerNegativePion();
            Piminus.CollectBestTriggerInfo(event); //calculate start time and obtain other relevant trigger information based off "best" pi minus
        }
            
    }
       
     
    public void CalcBeta2(DetectorParticle p){ //Maybe you can modify this so that speed of track can be calculated by any detector based off availabilitiy
        if(p.hasHit(DetectorType.FTOF, 2)==true){
            org.jlab.clas.detector.DetectorResponse res = p.getHit(DetectorType.FTOF, 2);
            double path = res.getPath();
            double time = res.getTime();
          //  double beta = path/(time-e.getEventTrigger().getStartTime())/29.9792;
          double beta = p.getPathLength(DetectorType.FTOF)/(time-event.getEventTrigger().getStartTime())/29.9792;
          //System.out.println(beta);
           // System.out.println(e.getEventTrigger().getStartTime());
            p.setBeta(beta);
            double mom = p.vector().mag();
            double mass2 = (mom*mom - beta*beta*mom*mom)/(beta*beta);
            p.setMass(mass2);
        }
    }
    
    public void CalcBetas(){
        for(int i = 0; i < event.getParticles().size() ; i++){
            DetectorParticle p = new DetectorParticle();
            p = event.getParticles().get(i);
            CalcBeta2(p);
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
    
    public void CollectBestTriggerInfo(DetectorEvent event){
                   EventTrigger Trigger = new EventTrigger();
                   Trigger = event.getEventTrigger();
                   DetectorParticle BestTrigger = Trigger.GetBestTriggerParticle(Trigger.getElectronCandidates());
                   Trigger.setTriggerParticle(BestTrigger);
                   Trigger.setzt(BestTrigger.vertex().z());
                   Trigger.setVertexTime(Trigger.VertexTime(BestTrigger, 11));
                   Trigger.setStartTime(Trigger.StartTime(BestTrigger,11)); //calculate start time using speed of an electron
                   
    }
}

class TriggerPositron implements BestTrigger {
    
    public void CollectBestTriggerInfo(DetectorEvent event){
                   EventTrigger Trigger = new EventTrigger();
                   Trigger = event.getEventTrigger();
                   DetectorParticle BestTrigger = Trigger.GetBestTriggerParticle(Trigger.getPositronCandidates());
                   Trigger.setTriggerParticle(BestTrigger);
                   Trigger.setVertexTime(Trigger.VertexTime(BestTrigger, 11));
                   Trigger.setStartTime(Trigger.StartTime(BestTrigger,11));
                   Trigger.setzt(BestTrigger.vertex().z());
                   
    }
}

class TriggerNegativePion implements BestTrigger {
    
    public void CollectBestTriggerInfo(DetectorEvent event){
                   EventTrigger Trigger = new EventTrigger();
                   Trigger = event.getEventTrigger();
                   DetectorParticle BestTrigger = Trigger.GetBestTriggerParticle(Trigger.getNegativePionCandidates());
                   Trigger.setTriggerParticle(BestTrigger);
                   Trigger.setVertexTime(Trigger.VertexTime(BestTrigger, 211));
                   Trigger.setStartTime(Trigger.StartTime(BestTrigger,211));
                   Trigger.setzt(BestTrigger.vertex().z());
                   
    }
}



class ElectronTriggerList implements TriggerCandidateList {
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

class PositronTriggerList implements TriggerCandidateList {
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

class NegativePionTriggerList implements TriggerCandidateList {
    public HashMap<Integer,DetectorParticle>  getCandidates(DetectorEvent event) {
        HashMap<Integer,DetectorParticle> map = new HashMap<Integer,DetectorParticle>();
            int mapiteration = 0;
            for(int i = 0 ; i < event.getParticles().size() ; i++){
                if(event.getParticles().get(i).getCharge()==-1 && event.getParticles().get(i).getScore()>=10 &&
                        event.getParticles().get(i).getScore()<=11 && event.getParticles().get(i).vector().mag()>3){
                    map.put(mapiteration,event.getParticles().get(i));
                    mapiteration = mapiteration + 1;
                }
            }
        return map;
    }
}
