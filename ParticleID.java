/*                                                                                                                                                             
 * To change this license header, choose License Headers in Project Properties.                                                                                
 * To change this template file, choose Tools | Templates                                                                                                      
 * and open the template in the editor.                                                                                                                        
 */
package org.jlab.service.eb;



/**                                                                                                                                                            
 *                                                                                                                                                             
 * @author jnewton                                                                                                                                            
 */
public interface ParticleID {
   
    PIDResult getPIDResult(DetectorParticle particle);
}
