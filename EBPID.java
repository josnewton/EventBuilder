/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.service.eb;

import static java.lang.Math.abs;
import java.util.HashMap;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import org.jlab.detector.base.DetectorType;
import org.jlab.io.base.DataEvent;

/**
 *
 * @author Joseph Newton
 */
public class EBPID {
    
    DetectorEvent event = new DetectorEvent();

    public EBPID(){
        
    }
    
    public void setEvent(DetectorEvent e){this.event = e;}

    
    
    public void DoTimeBasedPID() {
          PIDAssignment();
          CoincidenceChecks();
    }

    public void PIDAssignment() {

        for(int i = 0; i < event.getParticles().size(); i++){ 
                boolean haveID = false;
                TBElectron electron = new TBElectron();
                if(abs(electron.getPIDResult(event.getParticles().get(i)).getFinalID())==11 && haveID==false){ //Is it electron or positron?
                    event.getParticles().get(i).setPid(electron.getPIDResult(event.getParticles().get(i)).getFinalID());
                    haveID = true;
                }
                TBPion pion = new TBPion();
                if(abs(pion.getPIDResult(event.getParticles().get(i)).getFinalID())==211 && haveID==false){ //Is it a charged pion?
                    event.getParticles().get(i).setPid(pion.getPIDResult(event.getParticles().get(i)).getFinalID());
                    haveID = true;
                }
                TBKaon kaon = new TBKaon();
                if(abs(kaon.getPIDResult(event.getParticles().get(i)).getFinalID())==321 && haveID==false){ //Is it a charged kaon?
                    event.getParticles().get(i).setPid(kaon.getPIDResult(event.getParticles().get(i)).getFinalID());
                    haveID = true;
                }
                TBProton proton = new TBProton();
                if(abs(proton.getPIDResult(event.getParticles().get(i)).getFinalID())==2212 && haveID==false){  //Is it a proton or antiproton?
                    event.getParticles().get(i).setPid(proton.getPIDResult(event.getParticles().get(i)).getFinalID());
                    haveID = true;
                }
                
            }   
        }
    
    public void CoincidenceChecks() {
         HashMap<Integer,Double> ECTimingCheck = new HashMap<Integer,Double>();//Integer is Layer # for the Specified Detector
         HashMap<Integer,Double> FTOFTimingCheck = new HashMap<Integer,Double>();
         double t_0 = event.getEventTrigger().getStartTime();
            
            for(int i = 0 ; i < event.getParticles().size() ; i++) {
                DetectorParticle p = event.getParticles().get(i);
                
                ECTimingCheck.put(1,p.getVertexTime(DetectorType.EC,1));//PCAL Time Check
                ECTimingCheck.put(4,p.getVertexTime(DetectorType.EC,4));//ECINNER Time Check
                ECTimingCheck.put(7,p.getVertexTime(DetectorType.EC,7));//ECOUTER Time Check
                FTOFTimingCheck.put(2,p.getVertexTime(DetectorType.EC,2));//FTOF1B Time Check
                FTOFTimingCheck.put(3,p.getVertexTime(DetectorType.EC,3));//FTOF Panel 2 Time Check
                
                double chisquared = pow((ECTimingCheck.get(1)-t_0),2)/t_0 + pow((ECTimingCheck.get(4)-t_0),2)/t_0 +
                pow((ECTimingCheck.get(7)-t_0),2)/t_0 + pow((FTOFTimingCheck.get(2)-t_0),2)/t_0 + 
                pow((FTOFTimingCheck.get(3)-t_0),2)/t_0;
                
                p.getPIDResult().setECTimingCheck(ECTimingCheck);
                p.getPIDResult().setFTOFTimingCheck(FTOFTimingCheck);
                p.getPIDResult().setTimingQuality(chisquared);
            }
            
        }
    
    

 
}


class TBElectron implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PID.GetBeta(particle, 11));
                PID.setHTCC(PID.HTCCSignal(particle));
                PID.setCorrectSF(PID.SamplingFractionCheck(particle));
                PID.setHTCCThreshold(PID.HTCCThreshold(particle));
                

                
                HashMap<Integer,PIDExamination> ElectronTests= new HashMap<Integer,PIDExamination>();
                ElectronTests = PID.getElectronTests(particle);
                PIDResult Result = new PIDResult();
                for(int i = 0 ; i < ElectronTests.size() ; i++){
                    if(PID.compareExams(ElectronTests.get(i))==true){
                        Result.setFinalID(11*-particle.getCharge());
                        Result.setPIDExamination(PID);
                        break;
                    }
                    if(i == (ElectronTests.size()-1)){
                        Result.setFinalID(0);
                        Result.setPIDExamination(PID);
                    }
                }
                
                return Result;
            }
                        
    }


class TBPion implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PID.GetBeta(particle, 211));
                PID.setHTCC(PID.HTCCSignal(particle));
                PID.setCorrectSF(PID.SamplingFractionCheck(particle));
                PID.setHTCCThreshold(PID.HTCCThreshold(particle));
                
                HashMap<Integer,PIDExamination> PionTests= new HashMap<Integer,PIDExamination>();
                PionTests = PID.getPionTests(particle);
                PIDResult Result = new PIDResult();
                for(int i = 0 ; i < PionTests.size() ; i++){
                
                    if(PID.compareExams(PionTests.get(i))==true){
                        Result.setFinalID(211*particle.getCharge());
                        Result.setPIDExamination(PID);
                        break;
                    }
                    if(i == (PionTests.size()-1)){
                        Result.setFinalID(0);
                        Result.setPIDExamination(PID);
                    }
                }
                
                return Result;
            }
                        
    }

class TBKaon implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PID.GetBeta(particle, 321));
                PID.setHTCC(PID.HTCCSignal(particle));
                //PID.setLTCC(PIDAssignment.LTCCSignal(particle));
                
                HashMap<Integer,PIDExamination> KaonTests= new HashMap<Integer,PIDExamination>();
                KaonTests = PID.getKaonTests(particle);
                PIDResult Result = new PIDResult();
                for(int i = 0 ; i < KaonTests.size() ; i++){
                    if(PID.compareExams(KaonTests.get(i))==true){
                        Result.setFinalID(321*particle.getCharge());
                        Result.setPIDExamination(PID);
                        break;
                    }
                    if(i == (KaonTests.size()-1)){
                        Result.setFinalID(0);
                        Result.setPIDExamination(PID);
                    }
                }
                
                return Result;
            }
                        
    }

class TBProton implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PID.GetBeta(particle, 2212));
                PID.setHTCC(PID.HTCCSignal(particle));
                //PID.setLTCC(PIDAssignment.LTCCSignal(particle));
                
                HashMap<Integer,PIDExamination> ProtonTests= new HashMap<Integer,PIDExamination>();
                ProtonTests = PID.getProtonTests(particle);
                PIDResult Result = new PIDResult();
                for(int i = 0 ; i < ProtonTests.size() ; i++){
                    if(PID.compareExams(ProtonTests.get(i))==true){
                        Result.setFinalID(2212*particle.getCharge());
                        Result.setPIDExamination(PID);
                        break;
                    }
                    if(i == (ProtonTests.size()-1)){
                        Result.setFinalID(0);
                        Result.setPIDExamination(PID);
                    }
                }
                
                return Result;
            }
            
        }
