/*                                                                                                                                                             
 * To change this license header, choose License Headers in Project Properties.                                                                                
 * To change this template file, choose Tools | Templates                                                                                                      
 * and open the template in the editor.                                                                                                                        
 */
package org.jlab.clas.ebuilder;

import java.util.HashMap;



/**                                                                                                                                                            
 *                                                                                                                                                             
 * @author gavalian                                                                                                                                            
 */
public interface BestTrigger {
   
    void collectBestTriggerInfo(DetectorEvent event, HashMap<Integer,DetectorParticle> map);
}
