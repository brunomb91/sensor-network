package projects.LeaderElection.Sensors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import jsensor.nodes.Node;
import jsensor.nodes.messages.Inbox;
import jsensor.nodes.messages.Message;
import jsensor.runtime.Jsensor;
import projects.LeaderElection.Messages.LeaderElectionMessage;
import projects.LeaderElection.Timers.LeaderElectionTimer;

public class LeaderElectionNode extends Node {
	static Queue<Node> nodeQueue = new PriorityQueue<Node>();
    public LinkedList<Long> messagesIDs; 

	static HashMap<Node, Boolean> signal;
	static Node leader;
	static Node newLeader;
	
	@Override
	public void handleMessages(Inbox inbox) {
		while(inbox.hasMoreMessages()) {
			Message message = inbox.getNextMessage();
	          
	           if(message instanceof LeaderElectionMessage)
	           {
	        	   LeaderElectionMessage leaderElectionMessage = (LeaderElectionMessage) message;
	        	   
	               if(this.messagesIDs.contains(leaderElectionMessage.getID()))
	               {
	                   continue;
	               }
	               
	               this.messagesIDs.add(leaderElectionMessage.getID());
	               
	               if(leaderElectionMessage.getDestination().equals(this))
	               {
	            	   Jsensor.log("time: "+ Jsensor.currentTime +
	            			   "\t sensorID: " +this.ID+
	            			   "\t receivedFrom: " +leaderElectionMessage.getSender().getID() +
	            			   "\t hops: "+ leaderElectionMessage.getHops() +
	            			   "\t msg: " + leaderElectionMessage.getMsg().concat(this.ID+"") +
	            			   "\t leader: " +leaderElectionMessage.getLeaderMsg());
	               }
	               else
	               {
					    int n = 999999;
					    int cont=0;
					    for (int i=1;i<=n;i++ ){
					    	if(n%i == 0)
					    		cont=cont+1;
					    }
					    
					    if (cont > 0){
					    	leaderElectionMessage.setMsg(leaderElectionMessage.getMsg().concat(this.ID+ " - "));
		                   this.multicast(message);
					    }
	               }
	           }
			
			
		}
		
	}

	@Override
	public void onCreation() {
		//initializes the list of messages received by the node.
        this.messagesIDs = new LinkedList<Long>();
 
        //sends the first messages if is one of the selected nodes
        if(this.ID < 10)
        {
        	int time = 10 + this.ID * 10;
        	LeaderElectionTimer ft = new LeaderElectionTimer();
            ft.startRelative(time, this);
        }
		
	}	
	
}
