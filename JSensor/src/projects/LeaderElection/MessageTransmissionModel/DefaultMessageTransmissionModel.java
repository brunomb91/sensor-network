package projects.LeaderElection.MessageTransmissionModel;

import jsensor.nodes.Node;
import jsensor.nodes.messages.Message;
import jsensor.nodes.models.MessageTransmissionModel;

public class DefaultMessageTransmissionModel extends MessageTransmissionModel {

	@Override
	public float timeToReach(Node arg0, Node arg1, Message arg2) {
		// TODO Auto-generated method stub
		return 10;
	}

}
