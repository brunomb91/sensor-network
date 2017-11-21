package projects.LeaderElection.Sensors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import jsensor.nodes.Node;
import jsensor.nodes.messages.Inbox;
import jsensor.nodes.messages.Message;
import jsensor.runtime.Jsensor;
import projects.Flooding.Messages.FloodingMessage;
import projects.Flooding.Timers.FloodingTimer;
import projects.LeaderElection.Messages.LeaderElectionMessage;

public class LeaderElectionNode extends Node {
	Queue<Node> nodeQueue = new PriorityQueue<Node>();
    public LinkedList<Long> messagesIDs; 

	HashMap<Node, Boolean> signal;
	Node leader;
	Node newLeader;
	
	@Override
	public void handleMessages(Inbox inbox) {
		while(inbox.hasMoreMessages()) {
			Message message = inbox.getNextMessage();
	          
	           if(message instanceof FloodingMessage)
	           {
	        	   FloodingMessage floodingMessage = (FloodingMessage) message;
	        	   
	               if(this.messagesIDs.contains(floodingMessage.getID()))
	               {
	                   continue;
	               }
	               
	               this.messagesIDs.add(floodingMessage.getID());
	               
	               if(floodingMessage.getDestination().equals(this))
	               {
	            	   Jsensor.log("time: "+ Jsensor.currentTime +
	            			   "\t sensorID: " +this.ID+
	            			   "\t receivedFrom: " +floodingMessage.getSender().getID()+
	            			   "\t hops: "+ floodingMessage.getHops() +
	            			   "\t msg: " +floodingMessage.getMsg().concat(this.ID+""));
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
		            	   floodingMessage.setMsg(floodingMessage.getMsg().concat(this.ID+ " - "));
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
        	FloodingTimer ft = new FloodingTimer();
            ft.startRelative(time, this);
        }
		
	}
	
	/*
	 * Implementação do algoritmo de eleição de líder proposto no artigo
	 * Optimal Distributed Leader Election Algorithm
	 */
	
	public void leaderElectionOptimal(Node node, LeaderElectionMessage leaderElectionMessage) {
		HashMap<Node, Double> fail = new HashMap<Node, Double>();
		Node[] arrayQueue;
		int sizeOfQueue = nodeQueue.size();
		long delay = 2; 
		
		nodeQueue.add(node);
		fail.put(node, Math.random()); // Probabilidade de falha dos nós
		signal.put(node, false); // Variável Signal de cada nó alterada para false (ou No)
		arrayQueue = nodeQueue.toArray(new Node[sizeOfQueue]);	
		leader = arrayQueue[(int) Math.random() * sizeOfQueue]; // Selecionado aleatoriamente para enviar a mensagem LEADER
		
		for(int i=0; i<nodeQueue.size(); i++) {
			signal.replace(leader, true);
			
			if(fail.get(leader) >= 0.8) { // Supondo que os nós que tenham probabilidade de quebrar maior que 80%
				try {
					leader.wait(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				signal.replace(leader, false);
			}
			
			else {
				leaderElectionMessage.setLeaderMsg("Election");
				newLeader = nodeQueue.peek();
				signal.replace(leader, false);
			}
		}
	}
	
}
