/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import static java.lang.Math.abs;
import java.util.HashMap;
import org.jlab.clas.physics.Vector3;
import org.jlab.detector.base.DetectorType;
import org.jlab.geom.prim.Point3D;
import static java.lang.Math.abs;







/**
 *
 * @author gavalian
 */
public class EBPID {
    

    
    
    public static void doTimeBasedPID(DetectorEvent event) {
        int nparticles = event.getParticles().size(); //Charged Particle Identification for TB Tracks
        for(int i = 0; i < nparticles; i++){ 
        
 
                TBElectron electron = new TBElectron();
                if(abs(electron.getPIDResult(event.getParticles().get(i)).getFinalID())==11){
                    event.getParticles().get(i).setPIDResult(electron.getPIDResult(event.getParticles().get(i)));
                    break;
                }
                
                TBPion pion = new TBPion();
                if(abs(pion.getPIDResult(event.getParticles().get(i)).getFinalID())==211){
                    event.getParticles().get(i).setPIDResult(pion.getPIDResult(event.getParticles().get(i)));
                    break;
                }
                
                TBKaon kaon = new TBKaon();
                if(abs(kaon.getPIDResult(event.getParticles().get(i)).getFinalID())==321){
                    event.getParticles().get(i).setPIDResult(kaon.getPIDResult(event.getParticles().get(i)));
                    break;
                }
                
                TBProton proton = new TBProton();
                if(abs(proton.getPIDResult(event.getParticles().get(i)).getFinalID())==2212){
                    event.getParticles().get(i).setPIDResult(proton.getPIDResult(event.getParticles().get(i)));
                    break;
                }
                
            }
    }



 
}

