/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import java.util.ArrayList;
import java.util.List;
import org.jlab.detector.base.DetectorType;
import org.jlab.io.evio.EvioDataBank;
import org.jlab.io.evio.EvioDataEvent;
import org.jlab.io.evio.EvioFactory;




/**
 *
 * @author gavalian
 */
public class EventBuilderDetectors {
    

    
    public static  List<DetectorResponse>  getDetectorResponses(EvioDataEvent event){
        List<DetectorResponse> responses = new ArrayList<DetectorResponse>();
        List<DetectorResponse> calorimeter  = EventBuilderDetectors.getResponsesEC(event);
        List<DetectorResponse> timeofflight = EventBuilderDetectors.getResponsesTOF(event);
        List<CherenkovSignal> htcc   = EventBuilderDetectors.getSignalsHTCC(event);
        responses.addAll(calorimeter);
        responses.addAll(timeofflight);
    
        
        return responses;
    }
    
    public static  List<CherenkovSignal>  getCherenkovSignals(EvioDataEvent event){
        List<CherenkovSignal> signals = new ArrayList<CherenkovSignal>();
        List<CherenkovSignal> htcc   = EventBuilderDetectors.getSignalsHTCC(event);
        signals.addAll(htcc);

    
        
        return signals;
    }
    
    public static List<DetectorResponse>  getResponsesEC(EvioDataEvent event){
        
        List<DetectorResponse> responses = new ArrayList<DetectorResponse>();
        if(event.hasBank("ECDetector::clusters")==true){
            EvioDataBank bankEC = (EvioDataBank) event.getBank("ECDetector::clusters");
            int nrowsEC = bankEC.rows();
            for (int loop = 0; loop < nrowsEC; loop++){
                
                DetectorResponse  response = new DetectorResponse();
                response.getDescriptor().setType(DetectorType.EC);
                response.getDescriptor().setSectorLayerComponent(
								 bankEC.getInt("sector", loop),
								 bankEC.getInt("layer", loop)
								 , -1);
                
                response.setPosition(
				     bankEC.getDouble("X", loop), 
				     bankEC.getDouble("Y", loop), 
				     bankEC.getDouble("Z", loop)
				     );
                
        
                double energy = bankEC.getDouble("energy",loop);
             //  System.out.println(energy);
                response.setEnergy(energy);
                response.setTime(bankEC.getDouble("time", loop));
                responses.add(response);                
            }
        }
        return responses;
    }
    
    public static List<DetectorResponse>  getResponsesTOF(EvioDataEvent event){
        List<DetectorResponse> responses = new ArrayList<DetectorResponse>();
        if(event.hasBank("FTOFRec::ftofhits")==true){
            EvioDataBank bankFTOF = (EvioDataBank) event.getBank("FTOFRec::ftofhits");
            int nrowsFTOF = bankFTOF.rows();
            for (int i = 0; i < nrowsFTOF; i++){
                DetectorResponse  response = new DetectorResponse();
                response.getDescriptor().setType(DetectorType.FTOF);
                response.getDescriptor().setSectorLayerComponent(
								 bankFTOF.getInt("sector",i),
								 bankFTOF.getInt("panel_id", i),
								 bankFTOF.getInt("paddle_id", i)
								 );
                response.setPosition(
				     bankFTOF.getFloat("x", i), 
				     bankFTOF.getFloat("y", i), 
				     bankFTOF.getFloat("z", i)
				     );
                
                response.setEnergy(bankFTOF.getFloat("energy", i));
                response.setTime(bankFTOF.getFloat("time", i));
                responses.add(response);
            }
        }
        return responses;
    }
    
        public static List<CherenkovSignal>  getSignalsHTCC(EvioDataEvent event){
        List<CherenkovSignal> signals = new ArrayList<CherenkovSignal>();
        if(event.hasBank("HTCCRec::clusters")==true){
            EvioDataBank bankHTCC = (EvioDataBank) event.getBank("HTCCRec::clusters");
            int nrowsHTCC= bankHTCC.rows();
            for (int i = 0; i < nrowsHTCC; i++){
                CherenkovSignal  signal = new CherenkovSignal();
                String str = "htcc";
                signal.setSignalType(str);
                signal.setTheta(bankHTCC.getDouble("theta", i));
                signal.setPhi(bankHTCC.getDouble("phi", i));
                signal.setNphe(bankHTCC.getInt("nphe",i));
                signals.add(signal);
            }
        }
        return signals;
    }
    
    public static List<DetectorResponse>  getResponsesCTOF(EvioDataEvent event){
        List<DetectorResponse> responses = new ArrayList<DetectorResponse>();
                if(event.hasBank("CTOFRec::ftofhits")==true){
            EvioDataBank bankCTOF = (EvioDataBank) event.getBank("CTOFRec::ftofhits");
            int nrowsCTOF = bankCTOF.rows();
            for (int i = 0; i < nrowsCTOF; i++){
                DetectorResponse  response = new DetectorResponse();
                response.getDescriptor().setType(DetectorType.CTOF);
                response.getDescriptor().setSectorLayerComponent(
								 bankCTOF.getInt("sector",i),
								 bankCTOF.getInt("panel_id", i),
								 bankCTOF.getInt("paddle_id", i)
								 );
                response.setPosition(
				     bankCTOF.getFloat("x", i), 
				     bankCTOF.getFloat("y", i), 
				     bankCTOF.getFloat("z", i)
				     );
                
                response.setEnergy(bankCTOF.getFloat("energy", i));
                response.setTime(bankCTOF.getFloat("time", i));
                responses.add(response);
            }
        }
        return responses;
    }
    
    public static EvioDataBank createBank(List<DetectorResponse> responses){
        int nrows = responses.size();
        EvioDataBank bank = EvioFactory.createBank("EVENTHB::detector", nrows);
        for(int i = 0; i < nrows; i++){
            bank.setInt("index", i,i);
            bank.setInt("pindex", i,responses.get(i).getAssociation());
            bank.setInt("detector", i, responses.get(i).getDescriptor().getType().getDetectorId());
            bank.setInt("sector", i, responses.get(i).getDescriptor().getSector());
            bank.setInt("layer", i, responses.get(i).getDescriptor().getLayer());
            
            bank.setFloat("X", i, (float) responses.get(i).getPosition().x());
            bank.setFloat("Y", i, (float) responses.get(i).getPosition().y());
            bank.setFloat("Z", i, (float) responses.get(i).getPosition().z());
            
            bank.setFloat("hX", i, (float) responses.get(i).getMatchedPosition().x());
            bank.setFloat("hY", i, (float) responses.get(i).getMatchedPosition().y());
            bank.setFloat("hZ", i, (float) responses.get(i).getMatchedPosition().z());
            bank.setFloat("path", i, (float) responses.get(i).getPath());
            bank.setFloat("time", i, (float) responses.get(i).getTime());
            bank.setFloat("energy", i, (float) responses.get(i).getEnergy());
        }
        return bank;
    }
    
    public static List<DetectorResponse>  readDetectorResponses(EvioDataEvent event){
        List<DetectorResponse> responses = new ArrayList<DetectorResponse>();
        if(event.hasBank("EVENTHB::detector")==true){
            EvioDataBank  bank = (EvioDataBank) event.getBank("EVENTHB::detector");
            int nrows = bank.rows();
            for(int i = 0; i < nrows; i++){
                DetectorResponse res = new DetectorResponse();
                res.getDescriptor().setType(DetectorType.getType(bank.getInt("detector", i)));
                res.getDescriptor().setSectorLayerComponent(
							    bank.getInt("sector", i),
							    bank.getInt("layer", i),
                        -1
							    );
                
                res.setPosition(
				bank.getFloat("X", i),
				bank.getFloat("Y", i),
				bank.getFloat("Z", i)
				);
                res.getMatchedPosition().setXYZ(
						bank.getFloat("hX", i),
						bank.getFloat("hY", i),
						bank.getFloat("hZ", i)
						);
                /*
                System.out.println("Matched position = " 
                        + res.getMatchedPosition().x() + "  "
                        + res.getMatchedPosition().y() + "  "
                        + res.getMatchedPosition().z() + "  "
                );
                System.out.println(res);
                */
                res.setEnergy(bank.getFloat("energy", i));
                res.setTime(bank.getFloat("time", i));
                res.setAssociation(bank.getInt("pindex", i));
                
                responses.add(res);
            }
        }
        return responses;
    } 
}