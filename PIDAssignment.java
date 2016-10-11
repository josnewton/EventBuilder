/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.service.eb;

import static java.lang.Math.abs;
import java.util.HashMap;
import org.jlab.detector.base.DetectorType;





/**
 *
 * @author jnewton
 */
public class PIDAssignment {

}

class TBElectron implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PID.GetBeta(particle, 11));
                PID.setHTCC(PID.HTCCSignal(particle));
                PID.setCorrectSF(PID.SamplingFractionCheck(particle));
                PID.setHTCCThreshold(PID.HTCCThreshold(particle));
                

                
                HashMap<Integer,PIDExamination> ElectronTests= new HashMap<Integer,PIDExamination>();
                ElectronTests = PIDTestMaps.getElectronTests(particle);
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
                PionTests = PIDTestMaps.getPionTests(particle);
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
                KaonTests = PIDTestMaps.getKaonTests(particle);
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
                ProtonTests = PIDTestMaps.getProtonTests(particle);
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

