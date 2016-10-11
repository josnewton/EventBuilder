/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.service.eb;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jlab.detector.base.DetectorType;
import static java.lang.Math.abs;

/**
 *
 * @author Joseph Newton
 */
public class EBTrigger {
        
       public static void GetFinalTriggerInformation(DetectorEvent event)   {

           for(int i = 0 ; i < event.getParticles().size() ; i++) {
               TIDResult result = new TIDResult();
               result = TriggerAssignment.GetShoweringParticleEvidence(event.getParticles().get(i));
               event.getParticles().get(i).setTIDResult(result);
               //System.out.println("Particle " + i + "  " + event.getParticles().get(i).getNphe("htcc"));
           }
     
           for(int i = 0 ; i < event.getEventTrigger().getUserTriggerIDs().size() ; i++) {
               HashMap<Integer,DetectorParticle> TriggerCandidates = new HashMap<Integer,DetectorParticle>();
               TriggerCandidates = event.ObtainTriggerCandidates(event.getEventTrigger().getUserTriggerIDs().get(i));
 
               if(abs(event.getEventTrigger().getUserTriggerIDs().get(i))==11 && TriggerCandidates.size()>0){//Positrons or Electrons
                   TriggerElectron te = new TriggerElectron();
                   te.collectBestTriggerInfo(event, TriggerCandidates);//sets trigger info including the start time
                   break;
               }
               
               if(abs(event.getEventTrigger().getUserTriggerIDs().get(i))==211 && TriggerCandidates.size()>0){//Positive or Negative Pions
                   TriggerPion tpi = new TriggerPion();
                   tpi.collectBestTriggerInfo(event, TriggerCandidates);//sets trigger info including the start time
                   break;
               }
               
            }
        }

       
     
    public static void CalcBeta2(DetectorParticle p, DetectorEvent e){
        if(p.hasHit(DetectorType.FTOF, 2)==true){
            DetectorResponse res = p.getHit(DetectorType.FTOF, 2);
            double path = res.getPath();
            double time = res.getTime();
            double beta = path/(time-e.getEventTrigger().getStartTime())/29.9792;
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
        
        
}
