/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.service.eb;

import org.jlab.detector.base.DetectorType;





/**
 *
 * @author jnewton
 */
public class TriggerAssignment {

    public static Boolean SamplingFractionCheck(DetectorParticle particle){ //Checks if particle falls within electron sampling fraction
        double p =  particle.vector().mag();
        Boolean check = false;
        double sf_calc =  particle.CalculatedSF();
        double sf_expect =  particle.ParametrizedSF();
        double sf_sigma = particle.ParametrizedSigma();
         //     System.out.println("Sampling Fraction = " + sf_calc);
        if(sf_calc<=(sf_expect+5*sf_sigma) && sf_calc>=(sf_expect-5*sf_sigma) && p!=0.0){ //Is the calculated sampling fraction within 5 sigma
              check = true;
              }
           return check;
        }


    
    public static Boolean HTCCSignal(DetectorParticle particle){
        String str = "htcc";
        Boolean truth = false;
        if(particle.getNphe(str)>0){
            truth = true;
        }
        return truth;
    }

    
    public static TIDResult GetShoweringParticleEvidence(DetectorParticle particle) {  
               
        TIDExamination TID = new TIDExamination(); //This is the "DetectorParticle"s PID properties.
        TID.setCorrectSF(TriggerAssignment.SamplingFractionCheck(particle)); //Is the sampling fraction within +-5 Sigma?
        TID.setHTCC(TriggerAssignment.HTCCSignal(particle)); //Is there a signal in HTCC?
        TID.setFTOF(particle.hasHit(DetectorType.FTOF, 2));
               
        TIDResult Result = new TIDResult();
        Result.setScore(TID.getTriggerScore());//Trigger Score for Electron/Positron
        Result.setTIDExamination(TID);
                
           
        return Result;
            }
    
    
  }



                        
   


