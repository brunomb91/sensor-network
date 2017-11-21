package projects.LeaderElection.Timers;

import jsensor.nodes.Node;
import jsensor.nodes.events.TimerEvent;
import jsensor.runtime.Jsensor;
import jsensor.utils.GenerateFilesOmnet;
import projects.LeaderElection.Messages.LeaderElectionMessage;

public class LeaderElectionTimer extends TimerEvent {

	@Override
	public void fire() {
		Node destination = this.node.getRandomNode("LeaderElectionNode");
    	while(true){
    		if(destination == null) {
                destination = this.node.getRandomNode("LeaderElectionNode");
                continue;
            }     
            
    		if(this.node == destination){
    			destination = this.node.getRandomNode("LeaderElectionNode");
                continue;
    		}
    		break;
    	}
        
    	LeaderElectionMessage message = new LeaderElectionMessage(this.node, destination, 0, ""+this.node.getID(), this.node.toString(), this.node.getChunk());
        
        String messagetext = "Created by the sensor: "+Integer.toString(this.node.getID()) + " Path: ";
        
        message.setMsg(messagetext);
    	Jsensor.log("time: "+ Jsensor.currentTime +"\t sensorID: "+this.node.getID()+ "\t sendTo: " +destination.getID());
		
    	GenerateFilesOmnet.addStartNode(this.node.getID(), destination.getID(), Jsensor.currentTime);
	    this.node.multicast(message);
		
	}

}
