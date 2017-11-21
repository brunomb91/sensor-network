package projects.LeaderElection;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import jsensor.nodes.Node;
import jsensor.runtime.AbsCustomGlobal;
import jsensor.runtime.Jsensor;

public class CustomGlobal extends AbsCustomGlobal {
	Queue<Node> nodeQueue = new PriorityQueue<Node>();
	HashMap<Node, Boolean> signal;
	Node[] arrayQueue;
	Node leader;
	Node newLeader;
	HashMap<Node, Double> fail = new HashMap<Node, Double>();
	
	@Override
	public boolean hasTerminated() {
		return false;
	}

	@Override
	public void postRound() {
		
	}

	@Override
	public void postRun() {
		
	}

	@Override
	public void preRound() {
		for (Node i: nodeQueue) {
			fail.put(i, Math.random()); // Probabilidade de falha dos nós
		}
		
		for(int i=0; i<nodeQueue.size(); i++) {
			signal.replace(leader, true);
			
			if(fail.get(leader) >= 0.8) { // Supondo que os nós que tenham probabilidade de quebrar maior que 80%
				try {
					leader.wait(2L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				signal.replace(leader, false);
			}
			
			else {
				newLeader = nodeQueue.peek();
				signal.replace(leader, false);
			}
		}
	}

	@Override
	public void preRun() {
		for (int i = 0; i< Jsensor.numNodes; i++) {
			nodeQueue.add(Jsensor.getNodeByID(i));
			signal.put(Jsensor.getNodeByID(i), false); // Variável Signal de cada nó alterada para false (ou No)
		}
		arrayQueue = nodeQueue.toArray(new Node[nodeQueue.size()]);
		leader = arrayQueue[(int) Math.random() * nodeQueue.size()]; // Selecionado aleatoriamente para enviar a mensagem LEADER
	}

}
